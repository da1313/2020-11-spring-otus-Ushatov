package org.course.repository;

import org.course.domain.Genre;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface GenreRepository extends MongoRepository<Genre, String> {

    List<Genre> findByIdIn(List<String> ids);

}
