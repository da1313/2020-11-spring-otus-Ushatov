package org.course.keyholder.poller;

import org.course.keyholder.EntityName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

@Component
public class KeyPollerImpl implements KeyPoller{

    private final EntityManager entityManager;

    private final static String AUTHOR_SEQUENCE = "select nextval('author_id_seq')";
    private final static String GENRE_SEQUENCE = "select nextval('genre_id_seq')";
    private final static String BOOK_SEQUENCE = "select nextval('book_id_seq')";
    private final static String COMMENT_SEQUENCE = "select nextval('comment_id_seq')";
    private final static String SCORE_SEQUENCE = "select nextval('score_id_seq')";
    private final static String USER_SEQUENCE = "select nextval('user_id_seq')";

    private final Map<EntityName, String> map = new HashMap<>();

    @Autowired
    public KeyPollerImpl(EntityManager entityManager) {
        this.entityManager = entityManager;

        map.put(EntityName.AUTHOR, AUTHOR_SEQUENCE);
        map.put(EntityName.BOOK, BOOK_SEQUENCE);
        map.put(EntityName.GENRE, GENRE_SEQUENCE);
        map.put(EntityName.COMMENT, COMMENT_SEQUENCE);
        map.put(EntityName.SCORE, SCORE_SEQUENCE);
        map.put(EntityName.USER, USER_SEQUENCE);
    }

    @Override
    public long getNextKey(EntityName entityName) {
        return ((BigInteger) entityManager.createNativeQuery(map.get(entityName)).getSingleResult()).longValue();
    }

}
