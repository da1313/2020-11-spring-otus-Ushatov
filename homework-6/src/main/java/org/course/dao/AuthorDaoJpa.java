package org.course.dao;

import org.course.dao.interfaces.AuthorDao;
import org.course.domain.Author;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class AuthorDaoJpa implements AuthorDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Author save(Author author) {
        if (author.getId() <= 0){
            entityManager.persist(author);
            return author;
        } else {
            return entityManager.merge(author);
        }
    }

    @Override
    public Optional<Author> findById(long id) {
        return Optional.ofNullable(entityManager.find(Author.class, id));
    }

    @Override
    public List<Author> findAll() {
        return entityManager.createQuery("select a from Author a", Author.class)
                .getResultList();
    }

    @Override
    public void delete(Author author){
        entityManager.remove(author);
    }

    @Override
    public void deleteAll() {
        entityManager.createQuery("delete from Author a").executeUpdate();
    }

    @Override
    public long count() {
        return entityManager.createQuery("select count(a.id) from Author a", Long.class).getSingleResult();
    }
}
