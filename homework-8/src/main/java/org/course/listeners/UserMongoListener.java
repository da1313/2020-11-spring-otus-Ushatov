package org.course.listeners;

import lombok.AllArgsConstructor;
import org.course.domain.User;
import org.course.repository.CommentRepository;
import org.course.repository.ScoreRepository;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterDeleteEvent;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserMongoListener extends AbstractMongoEventListener<User> {

    private final CommentRepository commentRepository;

    private final ScoreRepository scoreRepository;

    @Override
    public void onAfterDelete(AfterDeleteEvent<User> event) {

        String userId = event.getSource().get("_id").toString();

        commentRepository.deleteByUserId(userId);

        scoreRepository.deleteByUserId(userId);

    }

}
