package org.course.service;

import org.course.dao.intefaces.GenreDao;
import org.course.domain.Genre;
import org.course.service.intefaces.GenreHandlerService;
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

import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Class GenreHandleServiceImpl")
@ExtendWith(MockitoExtension.class)
class GenreHandleServiceImplTest {
    public static final long GENRE_ID = 1L;
    public static final String GENRE_NAME = "A1";
    public static final long GENRE_COUNT = 1L;
    @Mock
    private GenreDao genreDao;
    @Captor
    private ArgumentCaptor<Genre> genreArgumentCaptor;
    @Captor
    private ArgumentCaptor<Long> longArgumentCaptor;

    @Test
    void shouldPassCorrectGenreToCreateMethod() {
        GenreHandlerService service = new GenreHandlerServiceImpl(genreDao);
        Genre genre = new Genre(GENRE_NAME);
        service.createGenre(GENRE_NAME);
        Mockito.verify(genreDao).create(genreArgumentCaptor.capture());
        Assertions.assertEquals(genre, genreArgumentCaptor.getValue());
    }

    @Test
    void shouldThrowExceptionIfGenreNotFoundOnRead() {
        GenreHandlerService service = new GenreHandlerServiceImpl(genreDao);
        Mockito.when(genreDao.findById(GENRE_ID)).thenReturn(Optional.empty());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.readGenre(GENRE_ID));
        Assertions.assertEquals("Genre with id " + GENRE_ID + " not found!", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionIfGenreNotFoundOnUpdate() {
        GenreHandlerService service = new GenreHandlerServiceImpl(genreDao);
        Mockito.when(genreDao.findById(GENRE_ID)).thenReturn(Optional.empty());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.updateGenre(GENRE_ID, GENRE_NAME));
        Assertions.assertEquals("Genre with id " + GENRE_ID + " not found!", exception.getMessage());
    }

    @Test
    void shouldPassCorrectGenreToUpdateMethod() {
        GenreHandlerService service = new GenreHandlerServiceImpl(genreDao);
        Genre genre = new Genre(GENRE_ID, GENRE_NAME);
        Mockito.when(genreDao.findById(GENRE_ID)).thenReturn(Optional.of(genre));
        service.updateGenre(GENRE_ID, GENRE_NAME);
        Mockito.verify(genreDao).update(genreArgumentCaptor.capture());
        Assertions.assertEquals(genre, genreArgumentCaptor.getValue());
    }

    @Test
    void shouldPassCorrectGenreToDeleteMethod() {
        GenreHandlerService service = new GenreHandlerServiceImpl(genreDao);
        service.deleteGenre(GENRE_ID);
        Mockito.verify(genreDao).deleteById(longArgumentCaptor.capture());
        Assertions.assertEquals(GENRE_ID, longArgumentCaptor.getValue());
    }

    @Test
    void deleteAllGenres() {
        GenreHandlerService service = new GenreHandlerServiceImpl(genreDao);
        service.deleteAllGenres();
        Mockito.verify(genreDao, Mockito.times(1)).delete();
    }

    @Test
    void countGenres() {
        GenreHandlerService service = new GenreHandlerServiceImpl(genreDao);
        Mockito.when(genreDao.count()).thenReturn(GENRE_COUNT);
        String actual = service.countGenres();
        Assertions.assertEquals(String.valueOf(GENRE_COUNT), actual);
    }
}