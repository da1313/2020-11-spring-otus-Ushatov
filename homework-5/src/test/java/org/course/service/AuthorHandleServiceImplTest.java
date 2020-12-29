package org.course.service;

import org.course.dao.intefaces.AuthorDao;
import org.course.domain.Author;
import org.course.service.intefaces.AuthorHandlerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Class AuthorHandleServiceImpl")
@ExtendWith(MockitoExtension.class)
class AuthorHandleServiceImplTest {

    public static final long AUTHOR_ID = 1L;
    public static final String AUTHOR_NAME = "A1";
    public static final long AUTHOR_COUNT = 1L;
    @Mock
    private AuthorDao authorDao;
    @Captor
    private ArgumentCaptor<Author> authorArgumentCaptor;
    @Captor
    private ArgumentCaptor<Long> longArgumentCaptor;

    @Test
    void shouldPassCorrectAuthorToCreateMethod() {
        AuthorHandlerService service = new AuthorHandlerServiceImpl(authorDao);
        Author author = new Author(AUTHOR_NAME);
        service.createAuthor(AUTHOR_NAME);
        Mockito.verify(authorDao).create(authorArgumentCaptor.capture());
        Assertions.assertEquals(author, authorArgumentCaptor.getValue());
    }

    @Test
    void shouldThrowExceptionIfAuthorNotFoundOnRead() {
        AuthorHandlerService service = new AuthorHandlerServiceImpl(authorDao);
        Mockito.when(authorDao.findById(AUTHOR_ID)).thenReturn(Optional.empty());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.readAuthor(AUTHOR_ID));
        Assertions.assertEquals("Author with id " + AUTHOR_ID + " not found!", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionIfAuthorNotFoundOnUpdate() {
        AuthorHandlerService service = new AuthorHandlerServiceImpl(authorDao);
        Mockito.when(authorDao.findById(AUTHOR_ID)).thenReturn(Optional.empty());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.updateAuthor(AUTHOR_ID, AUTHOR_NAME));
        Assertions.assertEquals("Author with id " + AUTHOR_ID + " not found!", exception.getMessage());
    }

    @Test
    void shouldPassCorrectAuthorToUpdateMethod() {
        AuthorHandlerService service = new AuthorHandlerServiceImpl(authorDao);
        Author author = new Author(AUTHOR_ID, AUTHOR_NAME);
        Mockito.when(authorDao.findById(AUTHOR_ID)).thenReturn(Optional.of(author));
        service.updateAuthor(AUTHOR_ID, AUTHOR_NAME);
        Mockito.verify(authorDao).update(authorArgumentCaptor.capture());
        Assertions.assertEquals(author, authorArgumentCaptor.getValue());
    }

    @Test
    void shouldPassCorrectAuthorToDeleteMethod() {
        AuthorHandlerService service = new AuthorHandlerServiceImpl(authorDao);
        service.deleteAuthor(AUTHOR_ID);
        Mockito.verify(authorDao).deleteById(longArgumentCaptor.capture());
        Assertions.assertEquals(AUTHOR_ID, longArgumentCaptor.getValue());
    }

    @Test
    void deleteAllAuthors() {
        AuthorHandlerService service = new AuthorHandlerServiceImpl(authorDao);
        service.deleteAllAuthors();
        Mockito.verify(authorDao, Mockito.times(1)).delete();
    }

    @Test
    void countAuthors() {
        AuthorHandlerService service = new AuthorHandlerServiceImpl(authorDao);
        Mockito.when(authorDao.count()).thenReturn(AUTHOR_COUNT);
        String actual = service.countAuthors();
        Assertions.assertEquals(String.valueOf(AUTHOR_COUNT), actual);
    }
}