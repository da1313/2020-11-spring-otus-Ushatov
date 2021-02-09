package org.course.listeners;

import org.assertj.core.api.Assertions;
import org.course.changelog.InitTestData;
import org.course.domain.Author;
import org.course.domain.Book;
import org.course.repository.AuthorRepository;
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


@DisplayName("Class AuthorMongoEventListener")
@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Import({EmbeddedMongoConfig.class, InitTestData.class, AuthorMongoEventListener.class})
class AuthorMongoEventListenerTest {

    public static final String NEW_AUTHOR_NAME = "NEW_AUTHOR_NAME";

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private MongoOperations mongoOperations;

    @Test
    void shouldSetToNullAuthorInBookCollectionWhenAuthorIsDeleted() {

        Author author = mongoOperations.findAll(Author.class).get(0);

        List<String> booksWithAuthorIds = mongoOperations.findAll(Book.class).stream()
                .filter(b -> b.getAuthor().equals(author)).map(Book::getId).collect(Collectors.toList());

        authorRepository.delete(author);

        booksWithAuthorIds.forEach(i -> Assertions.assertThat(mongoOperations.findById(i, Book.class))
                .extracting(Book::getAuthor).isNull());

    }


    @Test
    void shouldUpdateAuthorInBookCollectionWhenAuthorIsDeleted() {

        Author author = mongoOperations.findAll(Author.class).get(0);

        List<String> booksWithAuthorIds = mongoOperations.findAll(Book.class).stream()
                .filter(b -> b.getAuthor().equals(author)).map(Book::getId).collect(Collectors.toList());

        author.setName(NEW_AUTHOR_NAME);

        authorRepository.save(author);

        booksWithAuthorIds.forEach(i -> Assertions.assertThat(mongoOperations.findById(i, Book.class))
                .extracting(Book::getAuthor).extracting(Author::getName).isEqualTo(NEW_AUTHOR_NAME));

    }

}