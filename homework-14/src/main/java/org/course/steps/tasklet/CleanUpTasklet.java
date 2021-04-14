package org.course.steps.tasklet;

import lombok.RequiredArgsConstructor;
import org.course.keyholder.KeyHolder;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CleanUpTasklet implements Tasklet {

    private final List<KeyHolder<String, Long>> keyHolderList;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        keyHolderList.forEach(KeyHolder::clear);
        return RepeatStatus.FINISHED;
    }

}
