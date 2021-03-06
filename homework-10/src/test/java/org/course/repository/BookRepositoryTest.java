package org.course.repository;

import org.assertj.core.api.Assertions;
import org.course.changelog.TestDataInitializer;
import org.course.domain.*;
import org.course.api.pojo.BookShort;
import org.course.testconfig.EmbeddedMongoConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.annotation.DirtiesContext;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
@DisplayName("Class BookRepository")
@Import({EmbeddedMongoConfig.class, TestDataInitializer.class})
class BookRepositoryTest {

    public static final String AUTHOR_NAME = "TEST";
    public static final String GENRE_NAME = "TEST";
    public static final String NEW_GENRE_NAME = "NEW_GENRE_NAME";
    public static final String QUERY_TITLE = "B0";
    public static final String QUERY_AUTHOR = "A0";

    @Autowired
    private MongoOperations mongoOperations;

    @Autowired
    private BookRepository bookRepository;


    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
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

    @Test
    void shouldIncreaseCommentCountByBookId() {

        Book book = mongoOperations.findAll(Book.class).get(0);

        bookRepository.increaseCommentCountById(book.getId());

        Book actual = mongoOperations.findById(book.getId(), Book.class);

        Assertions.assertThat(actual).extracting(Book::getInfo).extracting(Info::getCommentCount)
                .isEqualTo(book.getInfo().getCommentCount() + 1);

    }

    @Test
    void shouldIncreaseScoreByBookIdAndScoreNumberCaseOne() {

        Book book = mongoOperations.findAll(Book.class).get(0);

        bookRepository.increaseScoreCount(book.getId(), ScoreNumber.SCORE_ONE);

        Book actual = mongoOperations.findById(book.getId(), Book.class);

        Assertions.assertThat(actual).extracting(Book::getInfo).extracting(Info::getScoreOneCount)
                .isEqualTo(book.getInfo().getScoreOneCount() + 1);

    }

    @Test
    void shouldIncreaseScoreByBookIdAndScoreNumberCaseOTwo() {

        Book book = mongoOperations.findAll(Book.class).get(0);

        bookRepository.increaseScoreCount(book.getId(), ScoreNumber.SCORE_TWO);

        Book actual = mongoOperations.findById(book.getId(), Book.class);

        Assertions.assertThat(actual).extracting(Book::getInfo).extracting(Info::getScoreTwoCount)
                .isEqualTo(book.getInfo().getScoreTwoCount() + 1);

    }

    @Test
    void shouldIncreaseScoreByBookIdAndScoreNumberCaseThree() {

        Book book = mongoOperations.findAll(Book.class).get(0);

        bookRepository.increaseScoreCount(book.getId(), ScoreNumber.SCORE_THREE);

        Book actual = mongoOperations.findById(book.getId(), Book.class);

        Assertions.assertThat(actual).extracting(Book::getInfo).extracting(Info::getScoreThreeCount)
                .isEqualTo(book.getInfo().getScoreThreeCount() + 1);

    }

    @Test
    void shouldIncreaseScoreByBookIdAndScoreNumberCaseFour() {

        Book book = mongoOperations.findAll(Book.class).get(0);

        bookRepository.increaseScoreCount(book.getId(), ScoreNumber.SCORE_FOUR);

        Book actual = mongoOperations.findById(book.getId(), Book.class);

        Assertions.assertThat(actual).extracting(Book::getInfo).extracting(Info::getScoreFourCount)
                .isEqualTo(book.getInfo().getScoreFourCount() + 1);

    }

    @Test
    void shouldIncreaseScoreByBookIdAndScoreNumberCaseFive() {

        Book book = mongoOperations.findAll(Book.class).get(0);

        bookRepository.increaseScoreCount(book.getId(), ScoreNumber.SCORE_FIVE);

        Book actual = mongoOperations.findById(book.getId(), Book.class);

        Assertions.assertThat(actual).extracting(Book::getInfo).extracting(Info::getScoreFiveCount)
                .isEqualTo(book.getInfo().getScoreFiveCount() + 1);

    }

    @Test
    void shouldFindAllBookShortUsingSpringRepo() {

        List<Book> bookList = mongoOperations.findAll(Book.class);

        List<BookShort> expected = bookList.stream().map(b -> new BookShort(b.getId(), b.getTitle(), b.getTime(), b.getAuthor(),
                b.getGenres(), b.getInfo().getCommentCount(), b.getInfo().getAvgScore())).collect(Collectors.toList());

        List<BookShort> actual = bookRepository.findAllBookShortAuto(PageRequest.of(0, 100));

        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void shouldFindAllBookShortUsingSpringCriteriaAPI() {
        List<Book> bookList = mongoOperations.findAll(Book.class);

        List<BookShort> expected = bookList.stream().map(b -> new BookShort(b.getId(), b.getTitle(), b.getTime(), b.getAuthor(),
                b.getGenres(), b.getInfo().getCommentCount(), b.getInfo().getAvgScore()))
                .sorted(Comparator.comparingDouble(BookShort::getAvgScore)).collect(Collectors.toList());

        List<BookShort> actual = bookRepository.findAllBookShortCriteria(PageRequest.of(0, 100, Sort.by("avgScore").ascending()));

        expected.forEach(b -> System.out.println(b.getAvgScore()));
        actual.forEach(b -> System.out.println(b.getAvgScore()));

        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void shouldFindAllBookShortUsingMongoDbClient() {
        List<Book> bookList = mongoOperations.findAll(Book.class);

        List<BookShort> expected = bookList.stream().map(b -> new BookShort(b.getId(), b.getTitle(), b.getTime(), b.getAuthor(),
                b.getGenres(), b.getInfo().getCommentCount(), b.getInfo().getAvgScore()))
                .sorted(Comparator.comparingDouble(BookShort::getAvgScore)).collect(Collectors.toList());

        List<BookShort> actual = bookRepository.findAllBookShortNative(PageRequest.of(0, 100, Sort.by("avgScore").ascending()));

        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void shouldFindAllBookShortByGenre() {

        Genre genre = mongoOperations.findAll(Genre.class).get(0);

        List<Book> bookList = mongoOperations.findAll(Book.class);

        List<BookShort> expected = bookList.stream().filter(b -> b.getGenres().contains(genre))
                .map(b -> new BookShort(b.getId(), b.getTitle(), b.getTime(), b.getAuthor(),
                b.getGenres(), b.getInfo().getCommentCount(), b.getInfo().getAvgScore())).collect(Collectors.toList());

        List<BookShort> actual = bookRepository.findAllBookShortByGenre(genre.getId(), PageRequest.of(0, 100));

        Assertions.assertThat(actual).isEqualTo(expected);

    }

    @Test
    void shouldFindAllBookShortByQueryTitle() {

        List<Book> bookList = mongoOperations.findAll(Book.class);

        List<BookShort> expected = bookList.stream()
                .filter(b -> b.getTitle().contains(QUERY_TITLE))
                .map(b -> new BookShort(b.getId(), b.getTitle(), b.getTime(), b.getAuthor(),
                b.getGenres(), b.getInfo().getCommentCount(), b.getInfo().getAvgScore())).collect(Collectors.toList());

        List<BookShort> actual = bookRepository.findAllBookShortByQuery(QUERY_TITLE, PageRequest.of(0, 100));

        Assertions.assertThat(actual).isEqualTo(expected);

    }

    @Test
    void shouldFindAllBookShortByQueryAuthorName() {

        List<Book> bookList = mongoOperations.findAll(Book.class);

        List<BookShort> expected = bookList.stream()
                .filter(b -> b.getAuthor().getName().contains(QUERY_AUTHOR))
                .map(b -> new BookShort(b.getId(), b.getTitle(), b.getTime(), b.getAuthor(),
                        b.getGenres(), b.getInfo().getCommentCount(), b.getInfo().getAvgScore())).collect(Collectors.toList());

        List<BookShort> actual = bookRepository.findAllBookShortByQuery(QUERY_AUTHOR, PageRequest.of(0, 100));

        Assertions.assertThat(actual).isEqualTo(expected);

    }

    @Test
    void shouldFindCountByGenres() {

        Genre genre = mongoOperations.findAll(Genre.class).get(0);

        List<Book> bookList = mongoOperations.findAll(Book.class);

        long expected = bookList.stream().filter(b -> b.getGenres().contains(genre)).count();

        long actual = bookRepository.findCountByGenres(genre.getId()).getValue();

        Assertions.assertThat(actual).isEqualTo(expected);

    }

    @Test
    void shouldFindCountByQuery() {

        List<Book> bookList = mongoOperations.findAll(Book.class);

        long expected = bookList.stream().filter(b -> b.getAuthor().getName().contains(QUERY_AUTHOR)).count();

        long actual = bookRepository.findCountByQuery(QUERY_AUTHOR).getValue();

        Assertions.assertThat(actual).isEqualTo(expected);

    }



}