package org.course.repositories;

import org.course.domain.Author;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;

public interface AuthorRepository extends ReactiveSortingRepository<Author, String> {
}
