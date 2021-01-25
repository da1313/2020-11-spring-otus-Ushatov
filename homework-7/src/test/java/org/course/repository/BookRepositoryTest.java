package org.course.repository;

import org.assertj.core.api.Assertions;
import org.course.domain.Book;
import org.course.repository.projections.BookStatistics;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@DataJpaTest
@DisplayName("Class BookRepository")
class BookRepositoryTest {

    public static final long BOOK_ID = 1L;
    public static final long EXPECTED_QUERY_COUNT = 1L;
    public static final int PAGE_SIZE = 3;
    public static final int PAGE_NUMBER = 0;
    public static final long EXPECTED_BOOK_COMMENTS_COUNT = 2L;
    public static final long EXPECTED_ONE = 0L;
    public static final long EXPECTED_TWO = 1L;
    public static final long EXPECTED_THREE = 1L;
    public static final long EXPECTED_FOUR = 1L;
    public static final long EXPECTED_FIVE = 0L;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void shouldReturnBookCommentCountAndScores() {
        Book expectedBook = entityManager.find(Book.class, BOOK_ID);
        entityManager.clear();
        SessionFactory sessionFactory = entityManager.getEntityManager().getEntityManagerFactory().unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);
        sessionFactory.getStatistics().clear();
        Optional<BookStatistics> actual = bookRepository.findBookStatistics(expectedBook);
        Assertions.assertThat(actual).isPresent().get().extracting(BookStatistics::getBook).isEqualTo(expectedBook);
        Assertions.assertThat(actual).isPresent().get().extracting(BookStatistics::getCommentCount).isEqualTo(EXPECTED_BOOK_COMMENTS_COUNT);
        Assertions.assertThat(actual).isPresent().get().extracting(BookStatistics::getOne).isEqualTo(EXPECTED_ONE);
        Assertions.assertThat(actual).isPresent().get().extracting(BookStatistics::getTwo).isEqualTo(EXPECTED_TWO);
        Assertions.assertThat(actual).isPresent().get().extracting(BookStatistics::getThree).isEqualTo(EXPECTED_THREE);
        Assertions.assertThat(actual).isPresent().get().extracting(BookStatistics::getFour).isEqualTo(EXPECTED_FOUR);
        Assertions.assertThat(actual).isPresent().get().extracting(BookStatistics::getFive).isEqualTo(EXPECTED_FIVE);
        Assertions.assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(EXPECTED_QUERY_COUNT);
    }

    @Test
    void shouldReturnBooksWithAuthors() {
        List<Book> expected = entityManager.getEntityManager().createQuery("select b from Book b", Book.class)
                .setFirstResult(PAGE_NUMBER * PAGE_SIZE)
                .setMaxResults(PAGE_SIZE)
                .getResultList();
        expected.forEach(b -> System.out.println(b.getAuthor()));
        entityManager.clear();
        SessionFactory sessionFactory = entityManager.getEntityManager().getEntityManagerFactory().unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);
        sessionFactory.getStatistics().clear();
        List<Book> actual = bookRepository.findAllWithAuthor(PageRequest.of(PAGE_NUMBER, PAGE_SIZE));
        Assertions.assertThat(actual).hasSize(expected.size()).isEqualTo(expected);
        Assertions.assertThat(actual).extracting(Book::getAuthor)
                .containsExactlyInAnyOrderElementsOf(expected.stream().map(Book::getAuthor).collect(Collectors.toList()));
        Assertions.assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(EXPECTED_QUERY_COUNT);
    }
}