package org.course.service;

import org.assertj.core.api.Assertions;
import org.course.dao.interfaces.GenreDao;
import org.course.domain.Genre;
import org.course.service.GenreHandlerServiceImpl;
import org.course.service.intefaces.GenreHandlerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;


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
        Genre genre = new Genre();
        genre.setName(GENRE_NAME);
        service.createGenre(GENRE_NAME);
        Mockito.verify(genreDao).save(genreArgumentCaptor.capture());
        Assertions.assertThat(genreArgumentCaptor.getValue()).isEqualTo(genre);
    }

    @Test
    void shouldThrowExceptionIfGenreNotFoundOnRead() {
        GenreHandlerService service = new GenreHandlerServiceImpl(genreDao);
        Mockito.when(genreDao.findById(GENRE_ID)).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> service.readGenre(GENRE_ID))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Genre with id " + GENRE_ID + " not found!");
    }

    @Test
    void shouldThrowExceptionIfGenreNotFoundOnUpdate() {
        GenreHandlerService service = new GenreHandlerServiceImpl(genreDao);
        Mockito.when(genreDao.findById(GENRE_ID)).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> service.updateGenre(GENRE_ID, GENRE_NAME))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Genre with id " + GENRE_ID + " not found!");
    }

    @Test
    void shouldPassCorrectGenreToUpdateMethod() {
        GenreHandlerService service = new GenreHandlerServiceImpl(genreDao);
        Genre genre = new Genre();
        genre.setId(GENRE_ID);
        genre.setName(GENRE_NAME);
        Mockito.when(genreDao.findById(GENRE_ID)).thenReturn(Optional.of(genre));
        service.updateGenre(GENRE_ID, GENRE_NAME);
        Mockito.verify(genreDao).save(genreArgumentCaptor.capture());
        Assertions.assertThat(genreArgumentCaptor.getValue()).isEqualTo(genre);
    }

    @Test
    void shouldPassCorrectGenreToDeleteMethod() {
        GenreHandlerService service = new GenreHandlerServiceImpl(genreDao);
        Genre genre = new Genre();
        genre.setId(GENRE_ID);
        genre.setName(GENRE_NAME);
        Mockito.when(genreDao.findById(GENRE_ID)).thenReturn(Optional.of(genre));
        service.deleteGenre(GENRE_ID);
        Mockito.verify(genreDao).delete(genreArgumentCaptor.capture());
        Assertions.assertThat(genreArgumentCaptor.getValue()).isEqualTo(genre);
    }

    @Test
    void deleteAllGenres() {
        GenreHandlerService service = new GenreHandlerServiceImpl(genreDao);
        service.deleteAllGenres();
        Mockito.verify(genreDao, Mockito.times(1)).deleteAll();
    }

    @Test
    void countGenres() {
        GenreHandlerService service = new GenreHandlerServiceImpl(genreDao);
        Mockito.when(genreDao.count()).thenReturn(GENRE_COUNT);
        String actual = service.countGenres();
        Assertions.assertThat(actual).isEqualTo(String.valueOf(GENRE_COUNT));
    }
}