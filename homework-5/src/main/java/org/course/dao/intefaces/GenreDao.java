package org.course.dao.intefaces;

import org.course.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreDao {

    Optional<Genre> findById(long id);

    List<Genre> findAll();

    void deleteById(long id);

    void delete();

    Long createAndIncrement(Genre genre);

    void update(Genre genre);

    long count();

}
