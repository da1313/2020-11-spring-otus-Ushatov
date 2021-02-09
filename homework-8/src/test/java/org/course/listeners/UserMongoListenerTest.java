package org.course.listeners;

import org.assertj.core.api.Assertions;
import org.course.changelog.InitTestData;
import org.course.domain.Comment;
import org.course.domain.Score;
import org.course.domain.User;
import org.course.repository.UserRepository;
import org.course.testconfig.EmbeddedMongoConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.stream.Collectors;

@DisplayName("Class UserMongoListener")
@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Import({EmbeddedMongoConfig.class, InitTestData.class, UserMongoListener.class})
class UserMongoListenerTest {

    @Autowired
    private MongoOperations mongoOperations;

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldDeleteRelatedCommentsAndScoresWhenUserIsDeleted() {

        User user = mongoOperations.findAll(User.class).get(0);

        List<Comment> userComments = mongoOperations.findAll(Comment.class)
                .stream().filter(c -> c.getUser().equals(user)).collect(Collectors.toList());

        List<Score> userScores = mongoOperations.findAll(Score.class)
                .stream().filter(s -> s.getUser().equals(user)).collect(Collectors.toList());

        userRepository.delete(user);

        List<Comment> actualCommentList = mongoOperations.findAll(Comment.class);

        List<Score> actualScoreList = mongoOperations.findAll(Score.class);

        Assertions.assertThat(actualCommentList).doesNotContainAnyElementsOf(userComments);

        Assertions.assertThat(actualScoreList).doesNotContainAnyElementsOf(userScores);

    }
}