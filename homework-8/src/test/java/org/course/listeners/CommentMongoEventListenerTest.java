package org.course.listeners;

import org.assertj.core.api.Assertions;
import org.course.changelog.InitTestData;
import org.course.domain.Book;
import org.course.domain.Comment;
import org.course.domain.Info;
import org.course.domain.User;
import org.course.repository.CommentRepository;
import org.course.testconfig.EmbeddedMongoConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.annotation.DirtiesContext;


@DisplayName("Class CommentMongoEventListener")
@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Import({EmbeddedMongoConfig.class, InitTestData.class, CommentMongoEventListener.class})
class CommentMongoEventListenerTest {

    public static final String NEW_COMMENT = "NEW_COMMENT";
    @Autowired
    private MongoOperations mongoOperations;

    @Autowired
    private CommentRepository commentRepository;

    @Test
    void shouldUpdateRelatedBookWhenCommentIsSaved() {

        Book book = mongoOperations.findAll(Book.class).get(0);

        User user = mongoOperations.findAll(User.class).get(0);

        Comment comment = Comment.of(NEW_COMMENT, user, book);

        commentRepository.save(comment);

        Book actual = mongoOperations.findById(book.getId(), Book.class);

        Assertions.assertThat(actual).extracting(Book::getInfo).extracting(Info::getCount).isEqualTo(book.getInfo().getCount() + 1);

    }
}