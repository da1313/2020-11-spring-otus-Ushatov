package org.course.steps;

import lombok.RequiredArgsConstructor;
import org.course.converters.EntityConverter;
import org.course.domain.nosql.UserNosql;
import org.course.domain.sql.User;
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
public class UserBatchConfig {

    private static final int CHUNK_SIZE = 10;

    private final EntityConverter<User, UserNosql> userForwardConverter;

    private final EntityConverter<UserNosql, User> userBackwardConverter;

    private final StepBuilderFactory stepBuilderFactory;

    private final EntityManager entityManager;

    private final MongoOperations mongoOperations;

    private final DataSource dataSource;


    @StepScope
    @Bean
    public HibernatePagingItemReader<User> sqlUserReader(
            @Value("#{jobParameters['" + JobForwardConfig.PAGE_SIZE_PARAM_NAME + "']}") long pageSize){
        SessionFactory sessionFactory = entityManager.getEntityManagerFactory().unwrap(SessionFactory.class);
        return new HibernatePagingItemReaderBuilder<User>().queryString("select u from User u")
                .useStatelessSession(false)
                .name("hp-author-reader")
                .pageSize((int) pageSize)
                .sessionFactory(sessionFactory)
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<User> sqlUserWriterJdbc(){
        return new JdbcBatchItemWriterBuilder<User>()
                .dataSource(dataSource)
                .sql("insert into users(id, name, password, is_active) values(?, ?, ?, ?)")
                .itemPreparedStatementSetter((item, ps) -> {
                    ps.setLong(1, item.getId());
                    ps.setString(2, item.getName());
                    ps.setString(3, item.getPassword());
                    ps.setBoolean(4, item.isActive());
                })
                .build();
    }

    @Bean
    public ItemProcessor<User, UserNosql> sqlToNosqlUserProcessor(){
        return userForwardConverter::convert;
    }

    @Bean
    public ItemProcessor<UserNosql, User> nosqlToSqlUserProcessor(){
        return userBackwardConverter::convert;
    }

    @StepScope
    @Bean
    public MongoItemReader<UserNosql> nosqlUserReaderM(
            @Value("#{jobParameters['" + JobForwardConfig.PAGE_SIZE_PARAM_NAME + "']}") long pageSize){
        Map<String, Sort.Direction> sort = new HashMap<>();
        sort.put("id", Sort.Direction.ASC);
        return new MongoItemReaderBuilder<UserNosql>()
                .name("m-user-reader")
                .template(mongoOperations)
                .jsonQuery("{}")
                .targetType(UserNosql.class)
                .sorts(sort)
                .pageSize((int) pageSize)
                .build();
    }

    @Bean
    public MongoItemWriter<UserNosql> nosqlUserWriter(){
        return new MongoItemWriterBuilder<UserNosql>().template(mongoOperations).build();
    }

    @Bean
    public Step sqlToNosqlUserStep(HibernatePagingItemReader<User> reader,
                                     ItemProcessor<User, UserNosql> processor,
                                     MongoItemWriter<UserNosql> writer){

        return stepBuilderFactory.get("sql-to-nosql-user")
                .<User, UserNosql>chunk(CHUNK_SIZE)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public Step nosqlToSqlUserStep(MongoItemReader<UserNosql> reader,
                                     ItemProcessor<UserNosql, User> processor,
                                     JdbcBatchItemWriter<User> writer){

        return stepBuilderFactory.get("nosql-to-sql-user")
                .<UserNosql, User>chunk(CHUNK_SIZE)
                .reader(reader)
                .processor(processor)
                .writer(writer)
//                .taskExecutor(new SimpleAsyncTaskExecutor())
                .build();
    }

}
