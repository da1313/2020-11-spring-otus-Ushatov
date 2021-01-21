package org.course.dao.interfaces;

import org.course.domain.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorDao {

    Author save(Author author);

    Optional<Author> findById(long id);

    List<Author> findAll();

    void delete(Author author);

    void deleteAll();

    long count();

}
