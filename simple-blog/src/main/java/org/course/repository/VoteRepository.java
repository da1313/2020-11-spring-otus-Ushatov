package org.course.repository;

import org.course.model.Vote;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface VoteRepository extends MongoRepository<Vote, String> {

    Optional<Vote> findByUserIdAndPostId(String userId, String postId);

}
