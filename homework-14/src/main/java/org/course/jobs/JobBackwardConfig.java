package org.course.jobs;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JobBackwardConfig {

    private final JobBuilderFactory jobBuilderFactory;

    private final Step nosqlToSqlAuthorStep;

    private final Step nosqlToSqlGenreStep;

    private final Step nosqlToSqlBookStep;

    private final Step nosqlToSqlUserStep;

    private final Step nosqlToSqlCommentStep;

    private final Step nosqlToSqlScoreStep;

    private final Step cleanUp;

    public JobBackwardConfig(JobBuilderFactory jobBuilderFactory,
                             @Qualifier("nosqlToSqlAuthorStep") Step nosqlToSqlAuthorStep,
                             @Qualifier("nosqlToSqlGenreStep") Step nosqlToSqlGenreStep,
                             @Qualifier("nosqlToSqlBookStep") Step nosqlToSqlBookStep,
                             @Qualifier("nosqlToSqlUserStep") Step nosqlToSqlUserStep,
                             @Qualifier("nosqlToSqlCommentStep") Step nosqlToSqlCommentStep,
                             @Qualifier("nosqlToSqlScoreStep") Step nosqlToSqlScoreStep,
                             @Qualifier("cleanUp") Step cleanUp) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.nosqlToSqlAuthorStep = nosqlToSqlAuthorStep;
        this.nosqlToSqlGenreStep = nosqlToSqlGenreStep;
        this.nosqlToSqlBookStep = nosqlToSqlBookStep;
        this.nosqlToSqlUserStep = nosqlToSqlUserStep;
        this.nosqlToSqlCommentStep = nosqlToSqlCommentStep;
        this.nosqlToSqlScoreStep = nosqlToSqlScoreStep;
        this.cleanUp = cleanUp;
    }

    @Bean
    public Job nosqlToSqlJob(){
        return jobBuilderFactory.get("nosql-to-sql")
                .start(nosqlToSqlAuthorStep)
                .next(nosqlToSqlGenreStep)
                .next(nosqlToSqlBookStep)
                .next(nosqlToSqlUserStep)
                .next(nosqlToSqlCommentStep)
                .next(nosqlToSqlScoreStep)
                .next(cleanUp)
                .build();
    }

}
