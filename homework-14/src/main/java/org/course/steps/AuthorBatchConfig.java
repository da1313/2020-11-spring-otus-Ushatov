package org.course.steps;

import lombok.RequiredArgsConstructor;
import org.course.converters.EntityConverter;
import org.course.domain.nosql.AuthorNosql;
import org.course.domain.sql.Author;
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
public class AuthorBatchConfig {

    private static final int CHUNK_SIZE = 10;

    private final EntityConverter<Author, AuthorNosql> authorForwardConverter;

    private final EntityConverter<AuthorNosql, Author> authorBackwardConverter;

    private final StepBuilderFactory stepBuilderFactory;

    private final EntityManager entityManager;

    private final MongoOperations mongoOperations;

    private final DataSource dataSource;

    @StepScope
    @Bean
    public HibernatePagingItemReader<Author> sqlAuthorReaderP(
            @Value("#{jobParameters['" + JobForwardConfig.PAGE_SIZE_PARAM_NAME + "']}") long pageSize){
        HibernatePagingItemReaderBuilder<Author> builder = new HibernatePagingItemReaderBuilder<>();
        SessionFactory sessionFactory = entityManager.getEntityManagerFactory().unwrap(SessionFactory.class);
        return builder.queryString("select a from Author a")
                .useStatelessSession(false)
                .name("hp-author-reader")
                .pageSize((int) pageSize)
                .sessionFactory(sessionFactory)
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<Author> sqlAuthorWriterJdbc(){
        return new JdbcBatchItemWriterBuilder<Author>()
                .dataSource(dataSource)
                .sql("insert into authors(id, name) values(?, ?)")
                .itemPreparedStatementSetter((item, ps) -> {
                    ps.setLong(1, item.getId());
                    ps.setString(2, item.getName());
                })
                .build();
    }

    @Bean
    public ItemProcessor<Author, AuthorNosql> sqlToNosqlAuthorProcessor(){
        return authorForwardConverter::convert;
    }

    @Bean
    public ItemProcessor<AuthorNosql, Author> nosqlToSqlAuthorProcessor(){
        return authorBackwardConverter::convert;
    }

    @StepScope
    @Bean
    public MongoItemReader<AuthorNosql> nosqlAuthorReaderM(
            @Value("#{jobParameters['" + JobForwardConfig.PAGE_SIZE_PARAM_NAME + "']}") long pageSize){
        Map<String, Sort.Direction> sort = new HashMap<>();
        sort.put("id", Sort.Direction.ASC);
        return new MongoItemReaderBuilder<AuthorNosql>()
                .name("m-author-reader")
                .template(mongoOperations)
                .jsonQuery("{}")
                .targetType(AuthorNosql.class)
                .sorts(sort)
                .pageSize((int) pageSize)
                .build();
    }

    @Bean
    public MongoItemWriter<AuthorNosql> nosqlAuthorWriterM(){
        return new MongoItemWriterBuilder<AuthorNosql>().template(mongoOperations).build();
    }

    @Bean
    public Step sqlToNosqlAuthorStep(HibernatePagingItemReader<Author> reader,
                                     ItemProcessor<Author, AuthorNosql> processor,
                                     MongoItemWriter<AuthorNosql> writer){

        return stepBuilderFactory.get("sql-to-nosql-author")
                .<Author, AuthorNosql>chunk(CHUNK_SIZE)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public Step nosqlToSqlAuthorStep(MongoItemReader<AuthorNosql> reader,
                                     ItemProcessor<AuthorNosql, Author> processor,
                                     JdbcBatchItemWriter<Author> writer){

        return stepBuilderFactory.get("nosql-to-sql-author")
                .<AuthorNosql, Author>chunk(CHUNK_SIZE)
                .reader(reader)
                .processor(processor)
                .writer(writer)
//                .taskExecutor(new SimpleAsyncTaskExecutor())
                .build();
    }

}
