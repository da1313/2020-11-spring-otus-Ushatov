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
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void shouldReturnBookCommentCountAndScores() {
        Book expectedBook = entityManager.find(Book.class, BOOK_ID);
        long expectedOne = entityManager.getEntityManager()
                .createQuery("select count(bs) from BookScore bs where bs.score = 1 and bs.book = :book", Long.class)
                .setParameter("book", expectedBook)
                .getSingleResult();
        long expectedTwo = entityManager.getEntityManager()
                .createQuery("select count(bs) from BookScore bs where bs.score = 2 and bs.book = :book", Long.class)
                .setParameter("book", expectedBook)
                .getSingleResult();
        long expectedThree = entityManager.getEntityManager()
                .createQuery("select count(bs) from BookScore bs where bs.score = 3 and bs.book = :book", Long.class)
                .setParameter("book", expectedBook)
                .getSingleResult();
        long expectedFour = entityManager.getEntityManager()
                .createQuery("select count(bs) from BookScore bs where bs.score = 4 and bs.book = :book", Long.class)
                .setParameter("book", expectedBook)
                .getSingleResult();
        long expectedFive = entityManager.getEntityManager()
                .createQuery("select count(bs) from BookScore bs where bs.score = 5 and bs.book = :book", Long.class)
                .setParameter("book", expectedBook)
                .getSingleResult();
        long expectedCommentCount = entityManager.getEntityManager()
                .createQuery("select count(bc) from BookComment bc where bc.book = :book", Long.class)
                .setParameter("book", expectedBook)
                .getSingleResult();
        entityManager.clear();
        SessionFactory sessionFactory = entityManager.getEntityManager().getEntityManagerFactory().unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);
        sessionFactory.getStatistics().clear();
        Optional<BookStatistics> actual = bookRepository.findBookStatistics(expectedBook);
        Assertions.assertThat(actual).isPresent().get().extracting(BookStatistics::getBook).isEqualTo(expectedBook);
        Assertions.assertThat(actual).isPresent().get().extracting(BookStatistics::getCommentCount).isEqualTo(expectedCommentCount);
        Assertions.assertThat(actual).isPresent().get().extracting(BookStatistics::getOne).isEqualTo(expectedOne);
        Assertions.assertThat(actual).isPresent().get().extracting(BookStatistics::getTwo).isEqualTo(expectedTwo);
        Assertions.assertThat(actual).isPresent().get().extracting(BookStatistics::getThree).isEqualTo(expectedThree);
        Assertions.assertThat(actual).isPresent().get().extracting(BookStatistics::getFour).isEqualTo(expectedFour);
        Assertions.assertThat(actual).isPresent().get().extracting(BookStatistics::getFive).isEqualTo(expectedFive);
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