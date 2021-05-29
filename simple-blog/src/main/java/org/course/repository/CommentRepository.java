package org.course.repository;

import org.course.model.Comment;
import org.course.repository.custom.CommentRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommentRepository extends MongoRepository<Comment, String>, CommentRepositoryCustom {

    Page<Comment> findByPostId(Pageable pageable, String postId);

    Page<Comment> findByUserId(Pageable pageable, String userId);

}
