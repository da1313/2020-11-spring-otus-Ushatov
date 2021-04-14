package org.course.steps;

import lombok.RequiredArgsConstructor;
import org.course.converters.EntityConverter;
import org.course.domain.nosql.ScoreNosql;
import org.course.domain.sql.Score;
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
public class ScoreBatchConfig {

    public static final int CHUNK_SIZE = 10;

    private final EntityConverter<Score, ScoreNosql> scoreForwardConverter;

    private final EntityConverter<ScoreNosql, Score> scoreBackwardConverter;

    private final StepBuilderFactory stepBuilderFactory;

    private final EntityManager entityManager;

    private final MongoOperations mongoOperations;

    private final DataSource dataSource;

    @StepScope
    @Bean
    public HibernatePagingItemReader<Score> sqlScoreReader(
            @Value("#{jobParameters['" + JobForwardConfig.PAGE_SIZE_PARAM_NAME + "']}") long pageSize){
        SessionFactory sessionFactory = entityManager.getEntityManagerFactory().unwrap(SessionFactory.class);
        return new HibernatePagingItemReaderBuilder<Score>()
                .queryString("select s from Score s left join fetch s.user left join fetch s.book")
                .useStatelessSession(false)
                .name("hp-score-reader")
                .pageSize((int) pageSize)
                .sessionFactory(sessionFactory)
                .build();
    }

    @Bean
    public ItemProcessor<Score, ScoreNosql> sqlToNosqlScoreProcessor(){
        return scoreForwardConverter::convert;
    }

    @Bean
    public ItemProcessor<ScoreNosql, Score> nosqlToSqlScoreProcessor(){
        return scoreBackwardConverter::convert;
    }

    @StepScope
    @Bean
    public MongoItemReader<ScoreNosql> nosqlScoreReader(
            @Value("#{jobParameters['" + JobForwardConfig.PAGE_SIZE_PARAM_NAME + "']}") long pageSize){
        Map<String, Sort.Direction> sort = new HashMap<>();
        sort.put("id", Sort.Direction.ASC);
        return new MongoItemReaderBuilder<ScoreNosql>()
                .name("m-score-reader")
                .template(mongoOperations)
                .jsonQuery("{}")
                .targetType(ScoreNosql.class)
                .sorts(sort)
                .pageSize((int) pageSize)
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<Score> sqlScoreWriter(){
        return new JdbcBatchItemWriterBuilder<Score>()
                .dataSource(dataSource)
                .sql("insert into scores(id, score, book_id, user_id) values(?, ?, ?, ?)")
                .itemPreparedStatementSetter((item, ps) -> {
                    ps.setLong(1, item.getId());
                    ps.setInt(2, item.getScore());
                    ps.setLong(3, item.getBook().getId());
                    ps.setLong(4, item.getUser().getId());
                }).build();
    }

    @Bean
    public MongoItemWriter<ScoreNosql> nosqlScoreWriter(){
        return new MongoItemWriterBuilder<ScoreNosql>().template(mongoOperations).build();
    }

    @Bean
    public Step sqlToNosqlScoreStep(HibernatePagingItemReader<Score> reader,
                                      ItemProcessor<Score, ScoreNosql> processor,
                                      MongoItemWriter<ScoreNosql> writer){

        return stepBuilderFactory.get("sql-to-nosql-comment")
                .<Score, ScoreNosql>chunk(CHUNK_SIZE)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public Step nosqlToSqlScoreStep(MongoItemReader<ScoreNosql> reader,
                                      ItemProcessor<ScoreNosql, Score> processor,
                                      JdbcBatchItemWriter<Score> writer){

        return stepBuilderFactory.get("nosql-to-sql-book")
                .<ScoreNosql, Score>chunk(CHUNK_SIZE)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

}
