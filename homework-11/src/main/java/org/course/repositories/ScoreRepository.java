package org.course.repositories;

import org.course.domain.Score;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;

public interface ScoreRepository extends ReactiveSortingRepository<Score, String> {
}
