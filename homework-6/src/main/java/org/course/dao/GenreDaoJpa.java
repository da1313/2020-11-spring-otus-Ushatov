package org.course.dao;

import org.course.dao.interfaces.GenreDao;
import org.course.domain.Genre;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class GenreDaoJpa implements GenreDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Genre save(Genre genre) {
        if (genre.getId() <= 0){
            entityManager.persist(genre);
            return genre;
        } else {
            return entityManager.merge(genre);
        }
    }

    @Override
    public Optional<Genre> findById(long id) {
        EntityGraph<?> entityGraph = entityManager.getEntityGraph("genre-entity-graph-with-books-author");
        return Optional.ofNullable(entityManager
                .createQuery("select g from Genre g where g.id = :id", Genre.class)
                .setParameter("id", id).setHint("javax.persistence.fetchgraph", entityGraph)
                .getSingleResult());
    }

    @Override
    public List<Genre> findAll() {
        EntityGraph<?> entityGraph = entityManager.getEntityGraph("genre-entity-graph-with-books-author");
        return entityManager.createQuery("select g from Genre g", Genre.class)
                .setHint("javax.persistence.fetchgraph", entityGraph)
                .getResultList();
    }

    @Override
    public void delete(Genre genre) {
        entityManager.remove(genre);
    }

    @Override
    public void deleteAll() {
        entityManager.createQuery("delete from Genre").executeUpdate();
    }

    @Override
    public long count() {
        return entityManager.createQuery("select count(g.id) from Genre g", Long.class).getSingleResult();
    }
}
