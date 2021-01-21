package org.course.dao.interfaces;

import org.course.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreDao {

    Genre save(Genre genre);

    Optional<Genre> findById(long id);

    List<Genre> findAll();

    void delete(Genre genre);

    void deleteAll();

    long count();

}
