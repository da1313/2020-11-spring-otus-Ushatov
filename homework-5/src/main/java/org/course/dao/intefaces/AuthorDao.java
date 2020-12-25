package org.course.dao.intefaces;

import org.course.domain.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorDao {

    Optional<Author> findById(long id);

    List<Author> findAll();

    void deleteById(long id);

    void delete();

    Long createAndIncrement(Author author);

    void update(Author author);

    long count();

}
