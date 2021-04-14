package org.course.steps;

import lombok.RequiredArgsConstructor;
import org.course.converters.EntityConverter;
import org.course.domain.nosql.GenreNosql;
import org.course.domain.sql.Genre;
import org.course.jobs.JobForwardConfig;
import org.hibernate.SessionFactory;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.data.builder.MongoItemReaderBuilder;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.item.database.HibernatePagingItemReader;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.HibernatePagingItemReaderBuilder;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class GenreBatchConfig {

    private static final int CHUNK_SIZE = 5;

    private final EntityConverter<Genre, GenreNosql> genreForwardConverter;

    private final EntityConverter<GenreNosql, Genre> genreBackwardConverter;

    private final StepBuilderFactory stepBuilderFactory;

    private final EntityManager entityManager;

    private final MongoOperations mongoOperations;

    private final DataSource dataSource;

    @StepScope
    @Bean
    public HibernatePagingItemReader<Genre> sqlGenreReaderP(
            @Value("#{jobParameters['" + JobForwardConfig.PAGE_SIZE_PARAM_NAME + "']}") long pageSize){
        HibernatePagingItemReaderBuilder<Genre> builder = new HibernatePagingItemReaderBuilder<>();
        SessionFactory sessionFactory = entityManager.getEntityManagerFactory().unwrap(SessionFactory.class);
        return builder.queryString("select g from Genre g")
                .useStatelessSession(true)
                .name("hp-genre-reader")
                .pageSize((int) pageSize)
                .sessionFactory(sessionFactory)
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<Genre> sqlGenreWriterJdbc(){
        return new JdbcBatchItemWriterBuilder<Genre>()
                .dataSource(dataSource)
                .sql("insert into genres(id, name) values(?, ?)")
                .itemPreparedStatementSetter((item, ps) -> {
                    ps.setLong(1, item.getId());
                    ps.setString(2, item.getName());
                })
                .build();
    }

    @Bean
    public ItemProcessor<Genre, GenreNosql> sqlToNosqlGenreProcessor(){
        return genreForwardConverter::convert;
    }

    @Bean
    public ItemProcessor<GenreNosql, Genre> nosqlToSqlGenreProcessor(){
        return genreBackwardConverter::convert;
    }

    @StepScope
    @Bean
    public MongoItemReader<GenreNosql> nosqlGenreReaderM(
            @Value("#{jobParameters['" + JobForwardConfig.PAGE_SIZE_PARAM_NAME + "']}") long pageSize){
        Map<String, Sort.Direction> sort = new HashMap<>();
        sort.put("id", Sort.Direction.ASC);
        return new MongoItemReaderBuilder<GenreNosql>()
                .name("m-genre-reader")
                .template(mongoOperations)
                .jsonQuery("{}")
                .targetType(GenreNosql.class)
                .sorts(sort)
                .pageSize((int) pageSize)
                .build();
    }

    @Bean
    public MongoItemWriter<GenreNosql> nosqlGenreWriterM(){
        return new MongoItemWriterBuilder<GenreNosql>().template(mongoOperations).build();
    }

    @Bean
    public Step sqlToNosqlGenreStep(HibernatePagingItemReader<Genre> reader,
                                    ItemProcessor<Genre, GenreNosql> processor,
                                    MongoItemWriter<GenreNosql> writer){

        return stepBuilderFactory.get("sql-to-nosql-genre")
                .<Genre, GenreNosql>chunk(CHUNK_SIZE)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public Step nosqlToSqlGenreStep(MongoItemReader<GenreNosql> reader,
                                    ItemProcessor<GenreNosql, Genre> processor,
                                    JdbcBatchItemWriter<Genre> writer){

        return stepBuilderFactory.get("nosql-to-sql-genre")
                .<GenreNosql, Genre>chunk(CHUNK_SIZE)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

}
