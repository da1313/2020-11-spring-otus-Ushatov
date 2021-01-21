package org.course.dao;

import org.assertj.core.api.Assertions;
import org.course.dao.interfaces.AuthorDao;
import org.course.domain.Author;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

@DisplayName("Class AuthorDaoJpa")
@DataJpaTest
@Import(AuthorDaoJpa.class)
class AuthorDaoJpaTest {

    public static final String NEW_AUTHOR_NAME = "AN";
    public static final long EXISTING_AUTHOR_ID = 1L;
    public static final int AUTHORS_COUNT = 4;
    public static final int EXPECTED_QUERY_COUNT = 2;
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private AuthorDao authorDao;

    @Test
    void shouldSaveNewEntity() {
        Author expected = new Author();
        expected.setName(NEW_AUTHOR_NAME);
        authorDao.save(expected);
        entityManager.flush();
        entityManager.clear();
        Author actual = entityManager.find(Author.class, expected.getId());
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void shouldUpdateEntity() {
        Author expected = entityManager.find(Author.class, EXISTING_AUTHOR_ID);
        expected.setName(NEW_AUTHOR_NAME);
        expected = authorDao.save(expected);
        entityManager.flush();
        entityManager.clear();
        System.out.println(expected.getBooks() == null);
        Author actual = entityManager.find(Author.class, EXISTING_AUTHOR_ID);
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void shouldFindEntityById() {
        Author expected = entityManager.find(Author.class, EXISTING_AUTHOR_ID);
        entityManager.clear();
        Optional<Author> actual = authorDao.findById(EXISTING_AUTHOR_ID);
        Assertions.assertThat(actual).isPresent().get().isEqualTo(expected);
    }

    @Test
    void shouldFindAllEntitiesAndIfNeededLoadBooksWithOneQuery() {
        List<Author> expected = entityManager.getEntityManager()
                .createQuery("select a from Author a", Author.class).getResultList();
        expected.forEach(author -> System.out.println(author.getBooks()));
        entityManager.clear();
        SessionFactory sessionFactory = entityManager
                .getEntityManager().getEntityManagerFactory().unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);
        sessionFactory.getStatistics().clear();
        List<Author> actual = authorDao.findAll();
        Assertions.assertThat(actual).usingRecursiveComparison().ignoringFieldsMatchingRegexes(".*genres", ".*comments").isEqualTo(expected);
        Assertions.assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(EXPECTED_QUERY_COUNT);
    }

    @Test
    void shouldDeleteEntity() {
        Author author = entityManager.find(Author.class, EXISTING_AUTHOR_ID);
        authorDao.delete(author);
        entityManager.flush();
        entityManager.clear();
        Author actual = entityManager.find(Author.class, EXISTING_AUTHOR_ID);
        Assertions.assertThat(actual).isNull();
    }

    @Test
    void shouldDeleteAllEntities() {
        authorDao.deleteAll();
        entityManager.flush();
        entityManager.clear();
        List<Author> expected = entityManager.getEntityManager()
                .createQuery("select a from Author a", Author.class).getResultList();
        Assertions.assertThat(expected).isEmpty();
    }

    @Test
    void shouldReturnEntityCount() {
        long actual = authorDao.count();
        Assertions.assertThat(actual).isEqualTo(AUTHORS_COUNT);
    }
}