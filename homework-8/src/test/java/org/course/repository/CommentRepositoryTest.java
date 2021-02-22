package org.course.repository;

import org.assertj.core.api.Assertions;
import org.course.changelog.TestDataInitializer;
import org.course.domain.Book;
import org.course.domain.Comment;
import org.course.domain.User;
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

@DisplayName("Class CommentRepository")
@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Import({EmbeddedMongoConfig.class, TestDataInitializer.class})
class CommentRepositoryTest {

    @Autowired
    private MongoOperations mongoOperations;

    @Autowired
    private CommentRepository commentRepository;

    @Test
    void shouldDeleteCommentsByUserId() {

        User user = mongoOperations.findAll(User.class).get(0);

        commentRepository.deleteByUserId(user.getId());

        List<Comment> commentList = mongoOperations.findAll(Comment.class);

        long count = commentList.stream().filter(c -> c.getUser().equals(user)).count();

        Assertions.assertThat(count).isEqualTo(0);

    }

    @Test
    void shouldDeleteCommentsByBookId() {

        Book book = mongoOperations.findAll(Book.class).get(0);

        commentRepository.deleteByBookId(book.getId());

        List<Comment> commentList = mongoOperations.findAll(Comment.class);

        long count = commentList.stream().filter(c -> c.getBook().equals(book)).count();

        Assertions.assertThat(count).isEqualTo(0);

    }
}