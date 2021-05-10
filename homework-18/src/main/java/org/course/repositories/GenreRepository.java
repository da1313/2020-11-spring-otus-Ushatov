package org.course.repositories;

import org.course.domain.Genre;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import reactor.core.publisher.Flux;

import java.util.List;

public interface GenreRepository extends ReactiveSortingRepository<Genre, String> {

    Flux<Genre> findByIdIn(List<String> ids);

}
