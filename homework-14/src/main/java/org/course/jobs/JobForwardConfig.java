package org.course.jobs;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableBatchProcessing
@Configuration
public class JobForwardConfig {

    public static final String PAGE_SIZE_PARAM_NAME = "pageSize";

    private final JobBuilderFactory jobBuilderFactory;

    private final Step sqlToNosqlAuthorStep;

    private final Step sqlToNosqlGenreStep;

    private final Step sqlToNosqlBookStep;

    private final Step sqlToNosqlUserStep;

    private final Step sqlToNosqlCommentStep;

    private final Step sqlToNosqlScoreStep;

    private final Step cleanUp;

    public JobForwardConfig(JobBuilderFactory jobBuilderFactory,
                            @Qualifier("sqlToNosqlAuthorStep") Step sqlToNosqlAuthorStep,
                            @Qualifier("sqlToNosqlGenreStep") Step sqlToNosqlGenreStep,
                            @Qualifier("sqlToNosqlBookStep") Step sqlToNosqlBookStep,
                            @Qualifier("sqlToNosqlUserStep") Step sqlToNosqlUserStep,
                            @Qualifier("sqlToNosqlCommentStep") Step sqlToNosqlCommentStep,
                            @Qualifier("sqlToNosqlScoreStep")Step sqlToNosqlScoreStep,
                            @Qualifier("cleanUp") Step cleanUp) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.sqlToNosqlAuthorStep = sqlToNosqlAuthorStep;
        this.sqlToNosqlGenreStep = sqlToNosqlGenreStep;
        this.sqlToNosqlBookStep = sqlToNosqlBookStep;
        this.sqlToNosqlUserStep = sqlToNosqlUserStep;
        this.sqlToNosqlCommentStep = sqlToNosqlCommentStep;
        this.sqlToNosqlScoreStep = sqlToNosqlScoreStep;
        this.cleanUp = cleanUp;
    }



    @Bean
    public Job sqlToNosqlJob(){
        return jobBuilderFactory.get("sql-to-nosql")
                .start(sqlToNosqlAuthorStep)
                .next(sqlToNosqlGenreStep)
                .next(sqlToNosqlBookStep)
                .next(sqlToNosqlUserStep)
                .next(sqlToNosqlCommentStep)
                .next(sqlToNosqlScoreStep)
                .next(cleanUp)
                .build();
    }

}
