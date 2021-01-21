package org.course.dao;

import lombok.AllArgsConstructor;
import org.course.dao.interfaces.BookCommentDao;
import org.course.domain.BookComment;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class BookCommentDaoJpa implements BookCommentDao {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public BookComment save(BookComment bookComment) {
        if (bookComment.getId() <= 0){
            entityManager.persist(bookComment);
            return bookComment;
        } else {
            return entityManager.merge(bookComment);
        }
    }

    @Override
    public Optional<BookComment> findById(long id) {
        EntityGraph<?> entityGraph = entityManager.getEntityGraph("book-comment-entity-with-book-author");
        Map<String, Object> hints = new HashMap<>();
        hints.put("javax.persistence.fetchgraph", entityGraph);
        return Optional.ofNullable(entityManager.find(BookComment.class, id, hints));
    }

    @Override
    public List<BookComment> findAll() {
        EntityGraph<?> entityGraph = entityManager.getEntityGraph("book-comment-entity-with-book-author");
        return entityManager
                .createQuery("select bc from BookComment bc left join fetch bc.book", BookComment.class)
                .setHint("javax.persistence.fetchgraph", entityGraph)
                .getResultList();

    }

    @Override
    public void delete(BookComment bookComment) {
        entityManager.remove(bookComment);
    }

    @Override
    public void deleteAll() {
        entityManager.createQuery("delete from BookComment").executeUpdate();
    }

    @Override
    public long count() {
        return entityManager.createQuery("select count(bc.id) from BookComment bc", Long.class).getSingleResult();
    }
}
