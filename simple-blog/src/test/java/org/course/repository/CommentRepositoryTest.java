package org.course.repository;

import org.assertj.core.api.Assertions;
import org.course.model.Comment;
import org.course.model.Post;
import org.course.model.User;
import org.course.model.embedded.UserShort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.PageRequest;

import java.util.List;


@DisplayName("PostRepository")
@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
class CommentRepositoryTest {
    public static final String NEW_FIRST_NAME = "XXXX";
    public static final String NEW_LASTNAME = "YYYY";
    public static final String NEW_AVAURL = "ZZZZ";
    public static final int OVER_COUNT = 1000;
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void name() {

        User user = userRepository.findById("12").get();

        user.setFirstName(NEW_FIRST_NAME);
        user.setLastName(NEW_LASTNAME);
        user.setAvatarUrl(NEW_AVAURL);

        userRepository.save(user);

        commentRepository.updateUserComments(user.getId(), NEW_FIRST_NAME, NEW_LASTNAME, NEW_AVAURL);

        List<Comment> content = commentRepository.findByUserId(PageRequest.of(0, OVER_COUNT), user.getId()).getContent();

        content.forEach(comment -> Assertions.assertThat(comment)
                .extracting(Comment::getUser)
                .extracting(UserShort::getFirstName).isEqualTo(NEW_FIRST_NAME));

        content.forEach(comment -> Assertions.assertThat(comment)
                .extracting(Comment::getUser)
                .extracting(UserShort::getLastName).isEqualTo(NEW_LASTNAME));

        content.forEach(comment -> Assertions.assertThat(comment)
                .extracting(Comment::getUser)
                .extracting(UserShort::getAvatarUrl).isEqualTo(NEW_AVAURL));


    }
}