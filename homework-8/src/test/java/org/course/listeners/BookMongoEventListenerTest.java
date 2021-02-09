package org.course.listeners;

import org.assertj.core.api.Assertions;
import org.course.changelog.InitTestData;
import org.course.domain.*;
import org.course.repository.BookRepository;
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


@DisplayName("Class BookMongoEventListener")
@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Import({EmbeddedMongoConfig.class, InitTestData.class, BookMongoEventListener.class})
class BookMongoEventListenerTest {

    public static final String NEW_AUTHOR = "NEW_AUTHOR";
    public static final String NEW_GENRE = "NEW_GENRE";
    public static final String NEW_BOOK = "NEW_BOOK";

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MongoOperations mongoOperations;

    @Test
    void shouldSaveAuthorAndGenresWhenBookIsSaved() {

        Author author = Author.of(NEW_AUTHOR);

        Genre genre = Genre.of(NEW_GENRE);

        Book book = Book.of(NEW_BOOK, author, List.of(genre));

        bookRepository.save(book);

        List<Author> authorList = mongoOperations.findAll(Author.class);

        List<Genre> genreList = mongoOperations.findAll(Genre.class);

        Assertions.assertThat(authorList).contains(author);

        Assertions.assertThat(genreList).contains(genre);

    }

    @Test
    void shouldDeleteRelatedCommentAndScoresWhenBookIsDeleted() {

        Book book = mongoOperations.findAll(Book.class).get(0);

        List<Comment> commentList = mongoOperations.findAll(Comment.class);

        List<Score> scoreList = mongoOperations.findAll(Score.class);

        List<Comment> bookComments = commentList.stream().filter(c -> c.getBook().equals(book)).collect(Collectors.toList());

        List<Score> bookScores = scoreList.stream().filter(s -> s.getBook().equals(book)).collect(Collectors.toList());

        bookRepository.delete(book);

        List<Comment> commentListAfterDelete = mongoOperations.findAll(Comment.class);

        List<Score> scoreListAfterDelete = mongoOperations.findAll(Score.class);

        Assertions.assertThat(commentListAfterDelete).doesNotContainAnyElementsOf(bookComments);

        Assertions.assertThat(scoreListAfterDelete).doesNotContainAnyElementsOf(bookScores);

    }


}