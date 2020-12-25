package org.course.dao;

import org.course.dao.intefaces.AuthorDao;
import org.course.domain.Author;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Class AuthorDaoJdbc:")
@JdbcTest
@Import(AuthorDaoJdbc.class)
class AuthorDaoJdbcTest {

    public static final String NEW_AUTHOR_NAME = "A3";
    public static final String FIRST_AUTHOR_NAME = "A1";
    public static final long FIRST_AUTHOR_ID = 1L;
    public static final String CHANGED_AUTHOR_NAME = "XXX";
    public static final int AUTHORS_COUNT = 2;
    @Autowired
    private AuthorDao authorDao;

    @Test
    void shouldFindEntityBySpecifiedId() {
        Author expected = new Author(FIRST_AUTHOR_ID, FIRST_AUTHOR_NAME);
        Optional<Author> author = authorDao.findById(FIRST_AUTHOR_ID);
        assertEquals(expected, author.get());
    }

    @Test
    void shouldFindAllEntities() {
        List<Author> expected = new ArrayList<>();
        for (long i = 1; i < AUTHORS_COUNT + 1; i++) {
            Author author = new Author(i, "A" + i);
            expected.add(author);
        }
        List<Author> all = authorDao.findAll();
        assertEquals(expected, all);
    }

    @Test
    void shouldDeleteEntityWithSpecifiedId() {
        authorDao.deleteById(1);
        Optional<Author> author = authorDao.findById(1);
        assertTrue(author.isEmpty());
    }

    @Test
    void shouldDeleteAllEntities() {
        authorDao.delete();
        List<Author> all = authorDao.findAll();
        assertTrue(all.isEmpty());
    }

    @Test
    void shouldCreateNewEntity() {
        Author author = new Author();
        author.setName(NEW_AUTHOR_NAME);
        Long id = authorDao.createAndIncrement(author);
        Author actual = authorDao.findById(id).get();
        assertEquals(author, actual);
    }

    @Test
    void shouldUpdateExistingEntity() {
        Author author = authorDao.findById(FIRST_AUTHOR_ID).get();
        author.setName(CHANGED_AUTHOR_NAME);
        authorDao.update(author);
        Author actual = authorDao.findById(FIRST_AUTHOR_ID).get();
        assertEquals(author, actual);
    }

    @Test
    void shouldThrowExceptionIfUpdatedEntityIdIsNotSet(){
        Author author = new Author();
        author.setName(NEW_AUTHOR_NAME);
        assertThrows(IllegalArgumentException.class, () -> authorDao.update(author));
    }

    @Test
    void shouldReturnEntityCount(){
        long count = authorDao.count();
        assertEquals(AUTHORS_COUNT, count);
    }
}