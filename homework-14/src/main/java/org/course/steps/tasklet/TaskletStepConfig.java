package org.course.steps.tasklet;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TaskletStepConfig {

    private final Tasklet cleanUpTasklet;

    private final StepBuilderFactory stepBuilderFactory;

    public TaskletStepConfig(@Qualifier("cleanUpTasklet") Tasklet cleanUpTasklet,
                             StepBuilderFactory stepBuilderFactory) {
        this.cleanUpTasklet = cleanUpTasklet;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public Step cleanUp(){
        return stepBuilderFactory.get("clean-up").tasklet(cleanUpTasklet).build();
    }

}
