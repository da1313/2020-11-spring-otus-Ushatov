package org.course.steps.tasklet;

import lombok.RequiredArgsConstructor;
import org.course.keyholder.EntityName;
import org.course.keyholder.KeyHolder;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class TableIdInitializerTasklet implements Tasklet {

    private final List<KeyHolder<String, Long>> keyHolderList;

    private final EntityManager entityManager;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        Map<EntityName, Long> startIdsMap = new HashMap<>();
        BigInteger bookId = (BigInteger) entityManager.createNativeQuery("select nextval('book_id_seq')").getSingleResult();
        BigInteger authorId = (BigInteger) entityManager.createNativeQuery("select nextval('author_id_seq')").getSingleResult();
        BigInteger genreId = (BigInteger) entityManager.createNativeQuery("select nextval('genre_id_seq')").getSingleResult();
        BigInteger commentId = (BigInteger) entityManager.createNativeQuery("select nextval('comment_id_seq')").getSingleResult();
        BigInteger scoreId = (BigInteger) entityManager.createNativeQuery("select nextval('score_id_seq')").getSingleResult();
        BigInteger userId = (BigInteger) entityManager.createNativeQuery("select nextval('user_id_seq')").getSingleResult();
        startIdsMap.put(EntityName.BOOK, bookId.longValue());
        startIdsMap.put(EntityName.AUTHOR, authorId.longValue());
        startIdsMap.put(EntityName.GENRE, genreId.longValue());
        startIdsMap.put(EntityName.COMMENT, commentId.longValue());
        startIdsMap.put(EntityName.SCORE, scoreId.longValue());
        startIdsMap.put(EntityName.USER, userId.longValue());
        keyHolderList.forEach(k -> Arrays.asList(EntityName.values()).forEach(e -> k.setKey(e, startIdsMap.get(e))));
        return RepeatStatus.FINISHED;
    }

}
