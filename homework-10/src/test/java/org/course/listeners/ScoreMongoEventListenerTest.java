package org.course.listeners;

import org.assertj.core.api.Assertions;
import org.course.changelog.TestDataInitializer;
import org.course.domain.Book;
import org.course.domain.Info;
import org.course.domain.Score;
import org.course.domain.User;
import org.course.repository.ScoreRepository;
import org.course.testconfig.EmbeddedMongoConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.annotation.DirtiesContext;

@DisplayName("Class ScoreMongoEventListener")
@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Import({EmbeddedMongoConfig.class, TestDataInitializer.class, ScoreMongoEventListener.class})
class ScoreMongoEventListenerTest {

    public static final int SCORE_VALUE = 5;
    @Autowired
    private MongoOperations mongoOperations;

    @Autowired
    private ScoreRepository scoreRepository;

    @Test
    void shouldUpdateRelatedBookWhenScoreIsSaved() {

        Book book = mongoOperations.findAll(Book.class).get(0);

        User user = mongoOperations.findAll(User.class).get(0);

        Score score = Score.of(user, book, SCORE_VALUE);

        scoreRepository.save(score);

        Book actual = mongoOperations.findById(book.getId(), Book.class);

        Assertions.assertThat(actual)
                .extracting(Book::getInfo)
                .extracting(Info::getScoreFiveCount).isEqualTo(book.getInfo().getScoreFiveCount() + 1);

    }

}