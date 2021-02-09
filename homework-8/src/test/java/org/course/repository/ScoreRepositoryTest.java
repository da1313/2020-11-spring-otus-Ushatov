package org.course.repository;

import org.assertj.core.api.Assertions;
import org.course.changelog.InitTestData;
import org.course.domain.Book;
import org.course.domain.Score;
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


@DisplayName("Class ScoreRepository")
@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Import({EmbeddedMongoConfig.class, InitTestData.class})
class ScoreRepositoryTest {

    @Autowired
    private MongoOperations mongoOperations;

    @Autowired
    private ScoreRepository scoreRepository;

    @Test
    void shouldDeleteScoresByUserId() {

        User user = mongoOperations.findAll(User.class).get(0);

        scoreRepository.deleteByUserId(user.getId());

        List<Score> scoreList = mongoOperations.findAll(Score.class);

        long count = scoreList.stream().filter(c -> c.getUser().equals(user)).count();

        Assertions.assertThat(count).isEqualTo(0);

    }

    @Test
    void shouldDeleteScoresByBookId() {

        Book book = mongoOperations.findAll(Book.class).get(0);

        scoreRepository.deleteByBookId(book.getId());

        List<Score> scoreList = mongoOperations.findAll(Score.class);

        long count = scoreList.stream().filter(c -> c.getBook().equals(book)).count();

        Assertions.assertThat(count).isEqualTo(0);

    }
}