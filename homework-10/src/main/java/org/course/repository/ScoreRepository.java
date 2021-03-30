package org.course.repository;

import org.course.domain.Score;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ScoreRepository extends MongoRepository<Score, String> {

    void deleteByUserId(String userId);

    void deleteByBookId(String bookId);

}
