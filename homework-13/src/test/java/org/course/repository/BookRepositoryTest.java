package org.course.repository;

import org.assertj.core.api.Assertions;
import org.course.domain.Book;
import org.course.domain.Genre;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;

import javax.persistence.criteria.CriteriaQuery;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@DisplayName("Class BookRepository")
@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest {

    public static final int OVERFLOW_TEST_DATA_SIZE = 100;
    public static final int EXPECTED_QUERY_COUNT_TWO = 2;
    public static final long BOOK_ID = 1L;
    public static final int EXPECTED_QUERY_COUNT_ONE = 1;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    void shouldFindBookById() {

        Book book = testEntityManager.find(Book.class, BOOK_ID);

        System.out.println(book.getAuthor() + "*" + book.getGenres());

        testEntityManager.clear();

        SessionFactory sessionFactory = testEntityManager.getEntityManager().getEntityManagerFactory().unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);
        sessionFactory.getStatistics().clear();

        Book actual = bookRepository.findByIdEager(BOOK_ID).orElseThrow();

        Assertions.assertThat(actual).isEqualTo(book);
        Assertions.assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(EXPECTED_QUERY_COUNT_ONE);
    }

    @Test
    void shouldFindBooks() {

        CriteriaQuery<Book> query = testEntityManager.getEntityManager().getCriteriaBuilder().createQuery(Book.class);
        List<Book> bookList = testEntityManager.getEntityManager().createQuery(query.select(query.from(Book.class))).getResultList();

        bookList.forEach(b -> System.out.println(b.getAuthor() + "*" + b.getGenres()));

        testEntityManager.clear();

        SessionFactory sessionFactory = testEntityManager.getEntityManager().getEntityManagerFactory().unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);
        sessionFactory.getStatistics().clear();

        List<Book> actual = bookRepository.findAllEager(PageRequest.of(0, OVERFLOW_TEST_DATA_SIZE)).toList();

        actual.forEach(b -> System.out.println(b.getAuthor() + "*" + b.getGenres()));

        Assertions.assertThat(actual).containsExactlyElementsOf(bookList);
        Assertions.assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(EXPECTED_QUERY_COUNT_TWO);

    }
}