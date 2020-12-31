package org.course.dao;

import org.course.dao.intefaces.GenreDao;
import org.course.domain.Genre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Class GenreDaoJdbc:")
@JdbcTest
@Import(GenreDaoJdbc.class)
class GenreDaoJdbcTest {

    private static final long GENRE_COUNT = 3;
    public static final String NEW_GENRE = "NEW_GENRE";
    @Autowired
    private GenreDao genreDao;
    private List<Genre> genres = new ArrayList<>();

    @BeforeEach
    void objectModelInit(){
        genres.add(null);
        for (long i = 1; i <= GENRE_COUNT; i++) {
            Genre genre = new Genre();
            genre.setId(i);
            genre.setName("G" + i);
            genres.add(genre);
        }
    }

    @Test
    void shouldFindEntityBySpecifiedId() {
        Genre expected = genres.get(1);
        Genre actual = genreDao.findById(1).orElseGet(() -> fail("Genre not found!"));
        assertEquals(expected, actual);
    }

    @Test
    void shouldFindAllEntities() {
        List<Genre> actual = genreDao.findAll();
        List<Genre> expected = List.of(genres.get(1), genres.get(2), genres.get(3));
        assertEquals(expected, actual);
    }

    @Test
    void shouldDeleteEntityWithSpecifiedId() {
        genreDao.deleteById(1);
        Optional<Genre> actual = genreDao.findById(1);
        assertTrue(actual.isEmpty());
    }

    @Test
    void shouldDeleteAllEntities() {
        genreDao.delete();
        List<Genre> actual = genreDao.findAll();
        assertTrue(actual.isEmpty());
    }

    @Test
    void shouldCreateNewEntity() {
        Genre expected = new Genre(NEW_GENRE);
        Long actualId = genreDao.create(expected);
        Genre actual = genreDao.findById(actualId).orElseGet(() -> fail("Genre not found!"));
        assertEquals(expected, actual);
    }

    @Test
    void shouldUpdateExistingEntity() {
        Genre expected = genreDao.findById(1).orElseGet(() -> fail("Genre not found!"));
        expected.setName(NEW_GENRE);
        genreDao.update(expected);
        Genre actual = genreDao.findById(1).orElseGet(() -> fail("Genre not found!"));
        assertEquals(expected, actual);
    }

    @Test
    void shouldThrowExceptionIfUpdatedEntityIdIsNotSet(){
        Genre genre = new Genre(NEW_GENRE);
        assertThrows(IllegalArgumentException.class, () -> genreDao.update(genre));
    }

    @Test
    void shouldReturnEntityCount(){
        long count = genreDao.count();
        assertEquals(GENRE_COUNT, count);
    }
}