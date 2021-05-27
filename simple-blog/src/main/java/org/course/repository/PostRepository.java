package org.course.repository;

import org.course.model.ModerationStatus;
import org.course.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;

public interface PostRepository extends MongoRepository<Post, String> {

    Page<Post> findByIsActiveAndModerationStatusAndPublicationTimeLessThanEqual(Pageable pageable,
                                                                                boolean isActive,
                                                                                ModerationStatus moderationStatus,
                                                                                LocalDateTime currentTime);

    Page<Post> findByUserIdAndIsActiveAndModerationStatusAndPublicationTimeLessThanEqual(Pageable pageable,
                                                                                         String id,
                                                                                         boolean isActive,
                                                                                         ModerationStatus moderationStatus,
                                                                                         LocalDateTime currentTime);

    Page<Post> findByUserIdAndIsActive(Pageable pageable, String id, boolean isActive);

    Page<Post> findByUserIdAndPublicationTimeGreaterThan(Pageable pageable, String id, LocalDateTime currentTime);

    Page<Post> findByUserId(Pageable pageable, String id);

}
