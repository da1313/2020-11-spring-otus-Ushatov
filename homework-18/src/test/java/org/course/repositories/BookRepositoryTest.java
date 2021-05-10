package org.course.repositories;

import org.assertj.core.api.Assertions;
import org.course.api.pojo.BookShort;
import org.course.domain.Book;
import org.course.domain.Genre;
import org.course.domain.embedded.Info;
import org.course.domain.embedded.ScoreNumber;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import testdata.TestDataLoader;

import java.util.List;
import java.util.stream.Collectors;

@DisplayName("Class BookRepository")
@DataMongoTest
@Import(TestDataLoader.class)
class BookRepositoryTest {

    private static final String QUERY_TITLE = "B0";
    private static final String QUERY_AUTHOR = "A0";

    @Autowired
    private ReactiveMongoOperations mongoOperations;

    @Autowired
    private BookRepository bookRepository;

    @Test
    void shouldIncreaseCommentCountByBookId() {

        Book book = mongoOperations.findAll(Book.class).blockFirst();

        bookRepository.increaseCommentCount(book.getId()).block();

        Book actual = mongoOperations.findById(book.getId(), Book.class).block();

        Assertions.assertThat(actual).extracting(Book::getInfo).extracting(Info::getCommentCount)
                .isEqualTo(book.getInfo().getCommentCount() + 1);

    }

    @Test
    void shouldIncreaseScoreByBookIdAndScoreNumberCaseOne() {

        Book book = mongoOperations.findAll(Book.class).blockFirst();

        bookRepository.increaseScoreCount(book.getId(), ScoreNumber.SCORE_ONE).block();

        Book actual = mongoOperations.findById(book.getId(), Book.class).block();

        Assertions.assertThat(actual).extracting(Book::getInfo).extracting(Info::getScoreOneCount)
                .isEqualTo(book.getInfo().getScoreOneCount() + 1);

    }

    @Test
    void shouldFindAllBookShortUsingSpringRepo() {

        List<Book> bookList = mongoOperations.findAll(Book.class).collectList().block();

        List<BookShort> expected = bookList.stream().map(b -> new BookShort(b.getId(), b.getTitle(), b.getTime(), b.getAuthor(),
                b.getGenres(), b.getInfo().getCommentCount(), b.getInfo().getAvgScore())).collect(Collectors.toList());

        List<BookShort> actual = bookRepository.findAllBookShortAuto(PageRequest.of(0, 100)).collectList().block();

        Assertions.assertThat(actual).isEqualTo(expected);

    }

    @Test
    void shouldFindAllBookShortByGenre() {

        Genre genre = mongoOperations.findAll(Genre.class).blockFirst();

        List<Book> bookList = mongoOperations.findAll(Book.class).collectList().block();

        List<BookShort> expected = bookList.stream().filter(b -> b.getGenres().contains(genre))
                .map(b -> new BookShort(b.getId(), b.getTitle(), b.getTime(), b.getAuthor(),
                        b.getGenres(), b.getInfo().getCommentCount(), b.getInfo().getAvgScore())).collect(Collectors.toList());

        List<BookShort> actual = bookRepository.findAllBookShortByGenre(genre.getId(), PageRequest.of(0, 100)).collectList().block();

        Assertions.assertThat(actual).isEqualTo(expected);

    }

    @Test
    void shouldFindCountByGenres() {

        Genre genre = mongoOperations.findAll(Genre.class).blockFirst();

        List<Book> bookList = mongoOperations.findAll(Book.class).collectList().block();

        long expected = bookList.stream().filter(b -> b.getGenres().contains(genre)).count();

        long actual = bookRepository.findCountByGenres(genre.getId()).block().getValue();

        Assertions.assertThat(actual).isEqualTo(expected);

    }

    @Test
    void shouldFindAllBookShortByQueryTitle() {

        List<Book> bookList = mongoOperations.findAll(Book.class).collectList().block();

        List<BookShort> expected = bookList.stream()
                .filter(b -> b.getTitle().contains(QUERY_TITLE))
                .map(b -> new BookShort(b.getId(), b.getTitle(), b.getTime(), b.getAuthor(),
                        b.getGenres(), b.getInfo().getCommentCount(), b.getInfo().getAvgScore())).collect(Collectors.toList());

        List<BookShort> actual = bookRepository.findAllBookShortByQuery(QUERY_TITLE, PageRequest.of(0, 100)).collectList().block();

        Assertions.assertThat(actual).isEqualTo(expected);

    }

    @Test
    void shouldFindAllBookShortByQueryAuthorName() {

        List<Book> bookList = mongoOperations.findAll(Book.class).collectList().block();

        List<BookShort> expected = bookList.stream()
                .filter(b -> b.getAuthor().getName().contains(QUERY_AUTHOR))
                .map(b -> new BookShort(b.getId(), b.getTitle(), b.getTime(), b.getAuthor(),
                        b.getGenres(), b.getInfo().getCommentCount(), b.getInfo().getAvgScore())).collect(Collectors.toList());

        List<BookShort> actual = bookRepository.findAllBookShortByQuery(QUERY_AUTHOR, PageRequest.of(0, 100)).collectList().block();

        Assertions.assertThat(actual).isEqualTo(expected);

    }

    @Test
    void findCountByQuery() {

        List<Book> bookList = mongoOperations.findAll(Book.class).collectList().block();

        long expected = bookList.stream().filter(b -> b.getAuthor().getName().contains(QUERY_AUTHOR)).count();

        long actual = bookRepository.findCountByQuery(QUERY_AUTHOR).block().getValue();

        Assertions.assertThat(actual).isEqualTo(expected);

    }
}