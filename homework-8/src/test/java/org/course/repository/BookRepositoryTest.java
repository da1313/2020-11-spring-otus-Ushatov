package org.course.repository;

import org.assertj.core.api.Assertions;
import org.course.changelog.InitTestData;
import org.course.domain.Author;
import org.course.domain.Book;
import org.course.domain.Genre;
import org.course.testconfig.EmbeddedMongoConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.annotation.DirtiesContext;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
@DisplayName("Class BookRepository")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Import({EmbeddedMongoConfig.class, InitTestData.class})
class BookRepositoryTest {

    public static final String AUTHOR_NAME = "TEST";
    public static final String GENRE_NAME = "TEST";
    public static final String NEW_GENRE_NAME = "NEW_GENRE_NAME";

    @Autowired
    private MongoOperations mongoOperations;

    @Autowired
    private BookRepository bookRepository;



    @Test
    void shouldDeleteAuthorFromAllBookByAuthorId() {

        Author author = mongoOperations.findAll(Author.class).get(0);

        List<String> bookIdsWithAuthor = mongoOperations.findAll(Book.class).stream()
                .filter(b -> b.getAuthor().equals(author)).map(Book::getId)
                .collect(Collectors.toList());

        bookRepository.deleteAuthorInBookCollection(author.getId());

        List<Book> actual = new ArrayList<>();

        bookIdsWithAuthor.forEach(i -> actual.add(mongoOperations.findById(i, Book.class)));

        actual.forEach(b -> Assertions.assertThat(b).extracting(Book::getAuthor).isNull());

    }

    @Test
    void shouldUpdateAuthorFromAllBookByAuthorId() {

        Author author = mongoOperations.findAll(Author.class).get(0);

        List<String> bookIdsWithAuthor = mongoOperations.findAll(Book.class).stream()
                .filter(b -> b.getAuthor().equals(author)).map(Book::getId)
                .collect(Collectors.toList());

        author.setName(AUTHOR_NAME);

        mongoOperations.save(author);

        bookRepository.updateAuthorInBookCollection(author);

        List<Book> actual = new ArrayList<>();

        bookIdsWithAuthor.forEach(i -> actual.add(mongoOperations.findById(i, Book.class)));

        actual.forEach(b -> Assertions.assertThat(b)
                .extracting(Book::getAuthor)
                .extracting(Author::getName).isEqualTo(AUTHOR_NAME));

    }

    @Test
    void shouldDeleteGenreFromAllBooks() {

        Genre genre = mongoOperations.findAll(Genre.class).get(0);

        List<String> bookIdsWithGenre = mongoOperations.findAll(Book.class).stream()
                .filter(b -> b.getGenres().contains(genre)).map(Book::getId)
                .collect(Collectors.toList());

        bookRepository.deleteGenreInBookCollectionById(genre.getId());

        List<Book> actual = new ArrayList<>();

        bookIdsWithGenre.forEach(i -> actual.add(mongoOperations.findById(i, Book.class)));

        actual.forEach(b -> Assertions.assertThat(b)
                .extracting(Book::getGenres).matches(g -> !g.contains(genre)));

    }

    @Test
    void shouldUpdateGenreInAllBooks() {

        Genre genre = mongoOperations.findAll(Genre.class).get(0);

        List<String> bookIdsWithGenre = mongoOperations.findAll(Book.class).stream()
                .filter(b -> b.getGenres().contains(genre)).peek(System.out::println).map(Book::getId)
                .collect(Collectors.toList());

        genre.setName(GENRE_NAME);

        mongoOperations.save(genre);

        bookRepository.updateGenreInBookCollection(genre);

        List<Book> actual = new ArrayList<>();

        bookIdsWithGenre.forEach(i -> actual.add(mongoOperations.findById(i, Book.class)));

        actual.forEach(b -> System.out.println(b.getGenres()));

        actual.forEach(b -> Assertions.assertThat(b)
                .extracting(Book::getGenres).matches(g -> g.contains(genre)));

    }

    @Test
    void shouldAddGenreToSpecifiedBook() {

        Genre genre = Genre.of(NEW_GENRE_NAME);

        mongoOperations.save(genre);

        Book book = mongoOperations.findAll(Book.class).get(0);

        bookRepository.addGenre(book, genre);

        Book actual = mongoOperations.findById(book.getId(), Book.class);

        Assertions.assertThat(actual).extracting(Book::getGenres).matches(genres -> genres.contains(genre));

    }

    @Test
    void shouldRemoveGenreFromSpecifiedBook() {

        Book book = mongoOperations.findAll(Book.class).get(0);

        Genre genre = book.getGenres().get(0);

        bookRepository.removeGenre(book, genre);

        Book actual = mongoOperations.findById(book.getId(), Book.class);

        Assertions.assertThat(actual).extracting(Book::getGenres).matches(genres -> !genres.contains(genre));

    }

}