package org.course.steps;

import lombok.RequiredArgsConstructor;
import org.course.converters.EntityConverter;
import org.course.domain.nosql.BookNosql;
import org.course.domain.sql.Book;
import org.course.jobs.JobForwardConfig;
import org.course.steps.writer.CustomBookReader;
import org.course.steps.writer.CustomBookWriter;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.data.builder.MongoItemReaderBuilder;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class BookBatchConfig {

    public static final int CHUNK_SIZE = 10;

    private final EntityConverter<Book, BookNosql> bookForwardConverter;

    private final EntityConverter<BookNosql, Book> bookBackwardConverter;

    private final StepBuilderFactory stepBuilderFactory;

    private final EntityManager entityManager;

    private final MongoOperations mongoOperations;

    private final CustomBookWriter customBookWriter;

    @StepScope
    @Bean
    public CustomBookReader sqlBookReaderC(
            @Value("#{jobParameters['" + JobForwardConfig.PAGE_SIZE_PARAM_NAME + "']}") long pageSize){
        CustomBookReader reader = new CustomBookReader(entityManager);
        reader.setPageSize((int) pageSize);
        return reader;
    }

    @Bean
    public ItemProcessor<Book, BookNosql> sqlToNosqlBookProcessor(){
        return bookForwardConverter::convert;
    }

    @Bean
    public ItemProcessor<BookNosql, Book> nosqlToSqlBookProcessor(){
        return bookBackwardConverter::convert;
    }

    @StepScope
    @Bean
    public MongoItemReader<BookNosql> nosqlBookReaderM(
            @Value("#{jobParameters['" + JobForwardConfig.PAGE_SIZE_PARAM_NAME + "']}") long pageSize){
        Map<String, Sort.Direction> sort = new HashMap<>();
        sort.put("id", Sort.Direction.ASC);
        return new MongoItemReaderBuilder<BookNosql>()
                .name("m-book-reader")
                .template(mongoOperations)
                .jsonQuery("{}")
                .targetType(BookNosql.class)
                .sorts(sort)
                .pageSize((int) pageSize)
                .build();
    }

    @Bean
    public MongoItemWriter<BookNosql> nosqlBookWriterM(){
        return new MongoItemWriterBuilder<BookNosql>().template(mongoOperations).build();
    }

    @Bean
    public Step sqlToNosqlBookStep(CustomBookReader reader,
                                   ItemProcessor<Book, BookNosql> processor,
                                   MongoItemWriter<BookNosql> writer){

        return stepBuilderFactory.get("sql-to-nosql-book")
                .<Book, BookNosql>chunk(CHUNK_SIZE)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public Step nosqlToSqlBookStep(MongoItemReader<BookNosql> reader,
                                   ItemProcessor<BookNosql, Book> processor){

        return stepBuilderFactory.get("nosql-to-sql-book")
                .<BookNosql, Book>chunk(CHUNK_SIZE)
                .reader(reader)
                .processor(processor)
                .writer(customBookWriter)
                .build();
    }

}
