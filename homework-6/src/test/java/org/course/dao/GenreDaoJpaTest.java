package org.course.dao;

import org.assertj.core.api.Assertions;
import org.course.dao.interfaces.GenreDao;
import org.course.domain.Genre;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

@DisplayName("Class GenreDaoJpa")
@DataJpaTest
@Import(GenreDaoJpa.class)
class GenreDaoJpaTest {

    public static final String NEW_GENRE_NAME = "GN";
    public static final long EXISTING_GENRE_ID = 1L;
    public static final int EXPECTED_QUERY_COUNT = 1;
    public static final int EXPECTED_GENRES_COUNT = 3;
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private GenreDao genreDao;

    @Test
    void shouldSaveNewEntity() {
        Genre expected = new Genre();
        expected.setName(NEW_GENRE_NAME);
        genreDao.save(expected);
        entityManager.flush();
        entityManager.clear();
        Genre actual = entityManager.find(Genre.class, expected.getId());
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void shouldUpdateEntity() {
        Genre expected = entityManager.find(Genre.class, EXISTING_GENRE_ID);
        expected.setName(NEW_GENRE_NAME);
        genreDao.save(expected);
        entityManager.flush();
        entityManager.clear();
        Genre actual = entityManager.find(Genre.class, EXISTING_GENRE_ID);
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void shouldFindEntityByIdAndLoadBookWithAuthor() {
        Genre expected = entityManager.find(Genre.class, EXISTING_GENRE_ID);
        System.out.println(expected.getBooks());
        entityManager.clear();
        SessionFactory sessionFactory = entityManager.getEntityManager().getEntityManagerFactory().unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);
        sessionFactory.getStatistics().clear();
        Optional<Genre> actual = genreDao.findById(EXISTING_GENRE_ID);
        Assertions.assertThat(actual).isPresent().get().isEqualTo(expected);
        Assertions.assertThat(actual).isPresent().get().extracting(Genre::getBooks)
                .matches(books -> books.size() == expected.getBooks().size() && books.containsAll(expected.getBooks()));
        Assertions.assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(EXPECTED_QUERY_COUNT);
    }

    @Test
    void shouldFindAllEntitiesByIdAndLoadBookWithAuthor() {
        List<Genre> expected = entityManager.getEntityManager().createQuery("select g from Genre g", Genre.class).getResultList();
        expected.forEach(genre -> System.out.println(genre.getBooks()));
        entityManager.clear();
        SessionFactory sessionFactory = entityManager.getEntityManager().getEntityManagerFactory().unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);
        sessionFactory.getStatistics().clear();
        List<Genre> actual = genreDao.findAll();
        Assertions.assertThat(actual).containsExactlyElementsOf(expected);
        actual.forEach(a -> Assertions.assertThat(a.getBooks())
                .containsExactlyElementsOf(expected.stream().filter(e -> e.getId() == a.getId()).findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("Genre with id " + a.getId() + " not found!"))
                        .getBooks()));
        Assertions.assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(EXPECTED_QUERY_COUNT);
    }

    @Test
    void shouldDeleteEntity() {
        Genre genre = entityManager.find(Genre.class, EXISTING_GENRE_ID);
        genreDao.delete(genre);
        entityManager.flush();
        entityManager.clear();
        Genre actual = entityManager.find(Genre.class, EXISTING_GENRE_ID);
        Assertions.assertThat(actual).isNull();
    }

    @Test
    void shouldDeleteAllEntities() {
        genreDao.deleteAll();
        entityManager.flush();
        entityManager.clear();
        List<Genre> actual = entityManager.getEntityManager().createQuery("select g from Genre g", Genre.class).getResultList();
        Assertions.assertThat(actual).isEmpty();
    }

    @Test
    void shouldReturnEntitiesCount() {
        long count = genreDao.count();
        Assertions.assertThat(count).isEqualTo(EXPECTED_GENRES_COUNT);
    }
}