package org.course.dao;

import org.course.dao.interfaces.BookDao;
import org.course.domain.Book;
import org.hibernate.annotations.QueryHints;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class BookDaoJpa implements BookDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Book save(Book book) {
        if (book.getId() <= 0){
            entityManager.persist(book);
            return book;
        } else {
            return entityManager.merge(book);
        }
    }

    @Override
    public Optional<Book> findById(long id) {
        return Optional.ofNullable(entityManager
                .createQuery("select b from Book b left join fetch b.author where b.id = :id", Book.class)
                .setParameter("id", id)
                .getSingleResult());
    }

    @Override
    public List<Book> findAll() {
        return entityManager.createQuery("select b from Book b left join fetch b.author", Book.class).getResultList();
    }

    @Override
    public void delete(Book book) {
        entityManager.remove(book);
    }

    @Override
    public void deleteAll() {
        entityManager.createQuery("delete from Book b").executeUpdate();
    }

    @Override
    public long count() {
        return entityManager.createQuery("select count(b.id) from Book b", Long.class).getSingleResult();
    }
}
