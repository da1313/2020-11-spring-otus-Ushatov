package org.course.steps;

import lombok.RequiredArgsConstructor;
import org.course.converters.EntityConverter;
import org.course.domain.nosql.CommentNosql;
import org.course.domain.sql.Comment;
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
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class CommentBatchConfig {

    public static final int CHUNK_SIZE = 10;

    private final EntityConverter<Comment, CommentNosql> commentForwardConverter;

    private final EntityConverter<CommentNosql, Comment> commentBackwardConverter;

    private final StepBuilderFactory stepBuilderFactory;

    private final EntityManager entityManager;

    private final MongoOperations mongoOperations;

    private final DataSource dataSource;

    @StepScope
    @Bean
    public HibernatePagingItemReader<Comment> sqlCommentReader(
            @Value("#{jobParameters['" + JobForwardConfig.PAGE_SIZE_PARAM_NAME + "']}") long pageSize){
        SessionFactory sessionFactory = entityManager.getEntityManagerFactory().unwrap(SessionFactory.class);
        return new HibernatePagingItemReaderBuilder<Comment>()
                .queryString("select c from Comment c left join fetch c.user left join fetch c.book")
                .useStatelessSession(false)
                .name("hp-comment-reader")
                .pageSize((int) pageSize)
                .sessionFactory(sessionFactory)
                .build();
    }

    @Bean
    public ItemProcessor<Comment, CommentNosql> sqlToNosqlCommentProcessor(){
        return commentForwardConverter::convert;
    }

    @Bean
    public ItemProcessor<CommentNosql, Comment> nosqlToSqlCommentProcessor(){
        return commentBackwardConverter::convert;
    }

    @StepScope
    @Bean
    public MongoItemReader<CommentNosql> nosqlCommentReader(
            @Value("#{jobParameters['" + JobForwardConfig.PAGE_SIZE_PARAM_NAME + "']}") long pageSize){
        Map<String, Sort.Direction> sort = new HashMap<>();
        sort.put("id", Sort.Direction.ASC);
        return new MongoItemReaderBuilder<CommentNosql>()
                .name("m-comment-reader")
                .template(mongoOperations)
                .jsonQuery("{}")
                .targetType(CommentNosql.class)
                .sorts(sort)
                .pageSize((int) pageSize)
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<Comment> sqlCommentWriter(){
        return new JdbcBatchItemWriterBuilder<Comment>()
                .dataSource(dataSource)
                .sql("insert into comments(id, text, time, book_id, user_id) values(?, ?, ?, ?, ?)")
                .itemPreparedStatementSetter((item, ps) -> {
                    ps.setLong(1, item.getId());
                    ps.setString(2, item.getText());
                    ps.setTimestamp(3, Timestamp.valueOf(item.getTime()));
                    ps.setLong(4, item.getBook().getId());
                    ps.setLong(5, item.getUser().getId());
                }).build();
    }

    @Bean
    public MongoItemWriter<CommentNosql> nosqlCommentWriter(){
        return new MongoItemWriterBuilder<CommentNosql>().template(mongoOperations).build();
    }

    @Bean
    public Step sqlToNosqlCommentStep(HibernatePagingItemReader<Comment> reader,
                                   ItemProcessor<Comment, CommentNosql> processor,
                                   MongoItemWriter<CommentNosql> writer){

        return stepBuilderFactory.get("sql-to-nosql-comment")
                .<Comment, CommentNosql>chunk(CHUNK_SIZE)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public Step nosqlToSqlCommentStep(MongoItemReader<CommentNosql> reader,
                                   ItemProcessor<CommentNosql, Comment> processor,
                                      JdbcBatchItemWriter<Comment> writer){

        return stepBuilderFactory.get("nosql-to-sql-book")
                .<CommentNosql, Comment>chunk(CHUNK_SIZE)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

}
