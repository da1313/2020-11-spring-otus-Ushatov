package org.course.dao;

import org.assertj.core.api.Assertions;
import org.checkerframework.checker.units.qual.A;
import org.course.dao.interfaces.BookDao;
import org.course.domain.Author;
import org.course.domain.Book;
import org.course.domain.Genre;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import javax.persistence.Persistence;
import javax.persistence.PersistenceUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@DisplayName("Class BookDaoJpa")
@DataJpaTest
@Import(BookDaoJpa.class)
class BookDaoJpaTest {
    public static final long AUTHOR_ID = 1L;
    public static final String NEW_BOOK_NAME = "BN";
    public static final long EXISTING_BOOK_ID = 1L;
    public static final int EXPECTED_QUERY_COUNT = 3;
    public static final int BOOK_COUNT = 6;
    public static final int EXPECTED_BOOK_COUNT = 18;
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private BookDao bookDao;

    @Test
    void shouldSaveNewEntity() {
        Author author = entityManager.find(Author.class, AUTHOR_ID);
        Book expected = new Book();
        expected.setName(NEW_BOOK_NAME);
        expected.setAuthor(author);
        bookDao.save(expected);
        entityManager.flush();
        entityManager.clear();
        Book actual = entityManager.find(Book.class, expected.getId());
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void shouldUpdateEntity() {
        Book expected = entityManager.find(Book.class, EXISTING_BOOK_ID);
        System.out.println(expected.getAuthor());
        expected.setName(NEW_BOOK_NAME);
        bookDao.save(expected);
        entityManager.flush();
        entityManager.clear();
        Book actual = entityManager.find(Book.class, EXISTING_BOOK_ID);
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void shouldFindEntityByIdWithAuthorAndLoadGenresAndCommentsWithOneQuery() {
        Book expected = entityManager.find(Book.class, EXISTING_BOOK_ID);
        System.out.println(expected.getAuthor());
        System.out.println(expected.getComments());
        System.out.println(expected.getGenres());
        entityManager.clear();
        SessionFactory sessionFactory = entityManager
                .getEntityManager().getEntityManagerFactory().unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);
        sessionFactory.getStatistics().clear();
        Optional<Book> actual = bookDao.findById(EXISTING_BOOK_ID);
        Assertions.assertThat(actual).isPresent().get().isEqualTo(expected);
        Assertions.assertThat(actual).isPresent().get().extracting(Book::getComments)
                .matches(bc -> bc.size() == expected.getComments().size() && expected.getComments().containsAll(bc));
        Assertions.assertThat(actual).isPresent().get().extracting(Book::getGenres)
                .matches(bg -> bg.size() == expected.getGenres().size() && expected.getGenres().containsAll(bg));
        Assertions.assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(EXPECTED_QUERY_COUNT);
    }

    @Test
    void shouldFindAllEntitiesByIdWithAuthorAndLoadGenresAndCommentsWithOneQuery() {
        List<Book> expected = entityManager.getEntityManager().createQuery("select b from Book b", Book.class).getResultList();
        expected.forEach(book -> System.out.println(book.getAuthor()));
        expected.forEach(book -> System.out.println(book.getComments()));
        expected.forEach(book -> System.out.println(book.getGenres()));
        entityManager.clear();
        SessionFactory sessionFactory = entityManager
                .getEntityManager().getEntityManagerFactory().unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);
        sessionFactory.getStatistics().clear();
        List<Book> actual = bookDao.findAll();
        Assertions.assertThat(actual).containsExactlyElementsOf(expected);
        expected.forEach(e -> Assertions.assertThat(e.getGenres())
                .containsExactlyElementsOf(actual.stream().filter(a -> a.getId() == e.getId()).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Book with id " + e.getId() + " not found!"))
                .getGenres()));
        expected.forEach(e -> Assertions.assertThat(e.getComments())
                .containsExactlyElementsOf(actual.stream().filter(a -> a.getId() == e.getId()).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Book with id " + e.getId() + " not found!"))
                .getComments()));
        Assertions.assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(EXPECTED_QUERY_COUNT);
    }

    @Test
    void shouldDeleteEntity() {
        Book book = entityManager.find(Book.class, EXISTING_BOOK_ID);
        bookDao.delete(book);
        entityManager.flush();
        entityManager.clear();
        Book actual = entityManager.find(Book.class, EXISTING_BOOK_ID);
        Assertions.assertThat(actual).isNull();
    }

    @Test
    void shouldDeleteAllEntities() {
        bookDao.deleteAll();
        List<Book> actual = entityManager.getEntityManager().createQuery("select b from Book b", Book.class).getResultList();
        Assertions.assertThat(actual).isEmpty();
    }

    @Test
    void shouldReturnEntitiesCount() {
        long count = bookDao.count();
        Assertions.assertThat(count).isEqualTo(EXPECTED_BOOK_COUNT);
    }

    @Test
    void name() {
        Book book = bookDao.findById(EXISTING_BOOK_ID).get();
        Genre genre = new Genre();
        genre.setName("CCC");
        entityManager.persistAndFlush(genre);
        book.addGenre(genre);
        entityManager.flush();
        entityManager.clear();
        Book actual = entityManager.find(Book.class, EXISTING_BOOK_ID);
        System.out.println(actual.getGenres());
        System.out.println(genre.getBooks());
    }
}