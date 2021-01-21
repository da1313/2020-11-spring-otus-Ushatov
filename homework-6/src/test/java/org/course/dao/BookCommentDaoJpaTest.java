package org.course.dao;

import org.assertj.core.api.Assertions;
import org.course.dao.interfaces.BookCommentDao;
import org.course.domain.Book;
import org.course.domain.BookComment;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;


@DisplayName("Class BookCommentDaoJpa")
@DataJpaTest
@Import(BookCommentDaoJpa.class)
class BookCommentDaoJpaTest {

    public static final long EXISTING_BOOK_ID = 1L;
    public static final String NEW_COMMENT_TEXT = "CN";
    public static final long EXISTING_BOOK_COMMENT_ID = 1L;
    public static final String NEW_TEXT = "NEW_TEXT";
    public static final long EXPECTED_QUERY_COUNT = 1L;
    public static final int BOOK_COMMENT_COUNT = 7;
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private BookCommentDao bookCommentDao;

    @Test
    void shouldSaveNewEntity() {
        Book book = entityManager.find(Book.class, EXISTING_BOOK_ID);
        System.out.println(book);
        BookComment expected = new BookComment();
        expected.setText(NEW_COMMENT_TEXT);
        expected.setBook(book);
        bookCommentDao.save(expected);
        entityManager.flush();
        entityManager.clear();
        BookComment actual = entityManager.find(BookComment.class, expected.getId());
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void shouldUpdateEntity() {
        BookComment expected = entityManager.find(BookComment.class, EXISTING_BOOK_COMMENT_ID);
        System.out.println(expected);
        expected.setText(NEW_TEXT);
        bookCommentDao.save(expected);
        entityManager.flush();
        entityManager.clear();
        BookComment actual = entityManager.find(BookComment.class, EXISTING_BOOK_COMMENT_ID);
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void shouldFindEntitiesByIdAndLoadBookWithAuthor() {
        BookComment expected = entityManager.getEntityManager().find(BookComment.class, EXISTING_BOOK_COMMENT_ID);
        System.out.println(expected.getBook());
        entityManager.clear();
        SessionFactory sessionFactory = entityManager
                .getEntityManager().getEntityManagerFactory().unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);
        sessionFactory.getStatistics().clear();
        Optional<BookComment> actual = bookCommentDao.findById(EXISTING_BOOK_COMMENT_ID);
        Assertions.assertThat(actual).isPresent().get().usingRecursiveComparison()
                .ignoringFieldsMatchingRegexes(".*genres", ".*comments", ".*books").isEqualTo(expected);
        Assertions.assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(EXPECTED_QUERY_COUNT);
    }

    @Test
    void shouldFindAllEntitiesByIdAndLoadBookWithAuthor() {
        List<BookComment> expected = entityManager.getEntityManager()
                .createQuery("select bc from BookComment bc", BookComment.class).getResultList();
        expected.forEach(bookComment -> System.out.println(bookComment.getBook()));
        entityManager.clear();
        SessionFactory sessionFactory = entityManager
                .getEntityManager().getEntityManagerFactory().unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);
        sessionFactory.getStatistics().clear();
        List<BookComment> actual = bookCommentDao.findAll();
        Assertions.assertThat(actual).usingRecursiveComparison()
                .ignoringFieldsMatchingRegexes(".*genres", ".*comments", ".*books").isEqualTo(expected);
        Assertions.assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(EXPECTED_QUERY_COUNT);
    }

    @Test
    void shouldDeleteEntity() {
        BookComment bookComment = entityManager.find(BookComment.class, EXISTING_BOOK_COMMENT_ID);
        bookCommentDao.delete(bookComment);
        entityManager.flush();
        entityManager.clear();
        BookComment actual = entityManager.find(BookComment.class, EXISTING_BOOK_COMMENT_ID);
        Assertions.assertThat(actual).isNull();
    }

    @Test
    void shouldDeleteAllEntities() {
        bookCommentDao.deleteAll();
        entityManager.flush();
        entityManager.clear();
        List<BookComment> actual = entityManager.getEntityManager()
                .createQuery("select bc from BookComment bc", BookComment.class).getResultList();
        Assertions.assertThat(actual).isEmpty();
    }

    @Test
    void shouldReturnEntitiesCount() {
        long count = bookCommentDao.count();
        Assertions.assertThat(count).isEqualTo(BOOK_COMMENT_COUNT);
    }
}