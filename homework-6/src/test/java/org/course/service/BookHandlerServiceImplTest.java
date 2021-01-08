package org.course.service;

import org.assertj.core.api.Assertions;
import org.course.dao.interfaces.AuthorDao;
import org.course.dao.interfaces.BookDao;
import org.course.dao.interfaces.GenreDao;
import org.course.domain.Author;
import org.course.domain.Book;
import org.course.domain.BookComment;
import org.course.domain.Genre;
import org.course.service.BookHandlerServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@DisplayName("Class BookHandlerServiceImpl")
@ExtendWith(MockitoExtension.class)
class BookHandlerServiceImplTest {

    public static final long AUTHOR_ID = 1L;
    public static final long GENRE_ID = 1L;
    public static final String AUTHOR_NAME = "A1";
    public static final String GENRE_NAME = "G1";
    public static final String BOOK_NAME = "B1";
    public static final long BOOK_ID = 1L;
    public static final long BOOK_COUNT = 1L;
    @Mock
    private BookDao bookDao;
    @Mock
    private AuthorDao authorDao;
    @Mock
    private GenreDao genreDao;
    @Mock
    private Book book;
    @Captor
    private ArgumentCaptor<Book> bookArgumentCaptor;
    @Captor
    private ArgumentCaptor<Genre> genreArgumentCaptor;

    @Test
    void shouldThrowExceptionIfAuthorNotFoundOnCreate() {
        BookHandlerServiceImpl service = new BookHandlerServiceImpl(bookDao, authorDao, genreDao);
        Mockito.when(authorDao.findById(AUTHOR_ID)).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> service.createBook(BOOK_NAME, String.valueOf(AUTHOR_ID), String.valueOf(GENRE_ID)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Author with id " + AUTHOR_ID + " not found!");
    }

    @Test
    void shouldThrowExceptionIfGenreNotFoundOnCreate() {
        BookHandlerServiceImpl service = new BookHandlerServiceImpl(bookDao, authorDao, genreDao);
        Author author = new Author();
        author.setId(AUTHOR_ID);
        author.setName(AUTHOR_NAME);
        Mockito.when(authorDao.findById(AUTHOR_ID)).thenReturn(Optional.of(author));
        Mockito.when(genreDao.findById(GENRE_ID)).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> service.createBook(BOOK_NAME, String.valueOf(AUTHOR_ID), String.valueOf(GENRE_ID)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Genre with id " + GENRE_ID + " not found!");
    }

    @Test
    void shouldThrowExceptionIfIdParseErrorOnCreate() {
        BookHandlerServiceImpl service = new BookHandlerServiceImpl(bookDao, authorDao, genreDao);
        Assertions.assertThatThrownBy(() -> service.createBook(BOOK_NAME, "1a", String.valueOf(GENRE_ID)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Parse error! For input string: \"1a\"");
    }

    @Test
    void shouldPassCorrectArgumentsToDaoMethodOnCreate(){
        BookHandlerServiceImpl service = new BookHandlerServiceImpl(bookDao, authorDao, genreDao);
        Author author = new Author();
        author.setId(AUTHOR_ID);
        author.setName(AUTHOR_NAME);
        Genre genre = new Genre();
        genre.setId(GENRE_ID);
        genre.setName(GENRE_NAME);
        Book book = new Book();
        book.setName(BOOK_NAME);
        book.setAuthor(author);
        book.getGenres().add(genre);
        Mockito.when(authorDao.findById(AUTHOR_ID)).thenReturn(Optional.of(author));
        Mockito.when(genreDao.findById(GENRE_ID)).thenReturn(Optional.of(genre));
        service.createBook(BOOK_NAME, String.valueOf(AUTHOR_ID), String.valueOf(GENRE_ID));
        Mockito.verify(bookDao).save(bookArgumentCaptor.capture());
        Assertions.assertThat(bookArgumentCaptor.getValue()).isEqualTo(book);
        Assertions.assertThat(bookArgumentCaptor.getValue().getGenres()).isEqualTo(book.getGenres());
    }

    @Test
    void shouldThrowExceptionIfBookNotFoundOnReadById() {
        BookHandlerServiceImpl service = new BookHandlerServiceImpl(bookDao, authorDao, genreDao);
        Mockito.when(bookDao.findById(BOOK_ID)).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> service.readBook(BOOK_ID))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Book with id " + BOOK_ID + " not found!");
    }

    @Test
    void shouldThrowExceptionIfBookNotFoundOnUpdate() {
        BookHandlerServiceImpl service = new BookHandlerServiceImpl(bookDao, authorDao, genreDao);
        Mockito.when(bookDao.findById(BOOK_ID)).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> service.updateBook(BOOK_ID, BOOK_NAME, AUTHOR_ID, AUTHOR_NAME))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Book with id " + BOOK_ID + " not found!");
    }

    @Test
    void shouldThrowExceptionIfAuthorNotFoundOnUpdate() {
        BookHandlerServiceImpl service = new BookHandlerServiceImpl(bookDao, authorDao, genreDao);
        Mockito.when(authorDao.findById(AUTHOR_ID)).thenReturn(Optional.empty());
        Mockito.when(bookDao.findById(BOOK_ID)).thenReturn(Optional.of(new Book()));
        Assertions.assertThatThrownBy(() -> service.updateBook(BOOK_ID, BOOK_NAME, AUTHOR_ID, AUTHOR_NAME))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Author with id " + AUTHOR_ID + " not found!");
    }

    @Test
    void shouldPassCorrectArgumentsToDaoMethodOnUpdate(){
        BookHandlerServiceImpl service = new BookHandlerServiceImpl(bookDao, authorDao, genreDao);
        Author author = new Author();
        author.setId(AUTHOR_ID);
        author.setName(AUTHOR_NAME);
        Book book = new Book();
        book.setName(BOOK_NAME);
        book.setAuthor(author);
        Mockito.when(authorDao.findById(AUTHOR_ID)).thenReturn(Optional.of(author));
        Mockito.when(bookDao.findById(BOOK_ID)).thenReturn(Optional.of(book));
        service.updateBook(BOOK_ID, BOOK_NAME, AUTHOR_ID, AUTHOR_NAME);
        Mockito.verify(bookDao).save(bookArgumentCaptor.capture());
        Assertions.assertThat(bookArgumentCaptor.getValue()).isEqualTo(book);
    }

    @Test
    void shouldPassCorrectArgumentsToDaoMethodOnDelete() {
        BookHandlerServiceImpl service = new BookHandlerServiceImpl(bookDao, authorDao, genreDao);
        Book book = new Book();
        book.setId(BOOK_ID);
        Mockito.when(bookDao.findById(BOOK_ID)).thenReturn(Optional.of(book));
        service.deleteBook(BOOK_ID);
        Mockito.verify(bookDao).delete(bookArgumentCaptor.capture());
        Assertions.assertThat(bookArgumentCaptor.getValue()).isEqualTo(book);
    }

    @Test
    void shouldInvokeCorrectDaoMethodOnDelete() {
        BookHandlerServiceImpl service = new BookHandlerServiceImpl(bookDao, authorDao, genreDao);
        service.deleteAllBooks();
        Mockito.verify(bookDao, Mockito.times(1)).deleteAll();
    }

    @Test
    void shouldReturnCorrectBookCount() {
        BookHandlerServiceImpl service = new BookHandlerServiceImpl(bookDao, authorDao, genreDao);
        Mockito.when(bookDao.count()).thenReturn(BOOK_COUNT);
        String actual = service.countBooks();
        Assertions.assertThat(actual).isEqualTo(String.valueOf(BOOK_COUNT));
    }

    @Test
    void shouldThrowExceptionIfBookNotFoundOnAddGenre() {
        BookHandlerServiceImpl service = new BookHandlerServiceImpl(bookDao, authorDao, genreDao);
        Mockito.when(bookDao.findById(BOOK_ID)).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> service.addGenre(BOOK_ID, GENRE_ID))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Book with id " + BOOK_ID + " not found!");
    }

    @Test
    void shouldThrowExceptionIfGenreNotFoundOnAddGenre() {
        BookHandlerServiceImpl service = new BookHandlerServiceImpl(bookDao, authorDao, genreDao);
        Mockito.when(bookDao.findById(BOOK_ID)).thenReturn(Optional.of(new Book()));
        Mockito.when(genreDao.findById(GENRE_ID)).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> service.addGenre(BOOK_ID, GENRE_ID))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Genre with id " + GENRE_ID + " not found!");
    }

    @Test
    void shouldPassCorrectArgumentsToAddGenreMethod() {
        Genre genre = new Genre();
        genre.setId(GENRE_ID);
        genre.setName(GENRE_NAME);
        BookHandlerServiceImpl service = new BookHandlerServiceImpl(bookDao, authorDao, genreDao);
        Mockito.when(bookDao.findById(BOOK_ID)).thenReturn(Optional.of(book));
        Mockito.when(genreDao.findById(GENRE_ID)).thenReturn(Optional.of(genre));
        service.addGenre(BOOK_ID, GENRE_ID);
        Mockito.verify(book).addGenre(genreArgumentCaptor.capture());
        Assertions.assertThat(genreArgumentCaptor.getValue()).isEqualTo(genre);
    }


    @Test
    void shouldThrowExceptionIfBookNotFoundOnRemoveGenre() {
        BookHandlerServiceImpl service = new BookHandlerServiceImpl(bookDao, authorDao, genreDao);
        Mockito.when(bookDao.findById(BOOK_ID)).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> service.removeGenre(BOOK_ID, GENRE_ID))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Book with id " + BOOK_ID + " not found!");
    }

    @Test
    void shouldThrowExceptionIfGenreNotFoundOnRemoveGenre() {
        BookHandlerServiceImpl service = new BookHandlerServiceImpl(bookDao, authorDao, genreDao);
        Mockito.when(bookDao.findById(BOOK_ID)).thenReturn(Optional.of(new Book()));
        Mockito.when(genreDao.findById(GENRE_ID)).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> service.removeGenre(BOOK_ID, GENRE_ID))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Genre with id " + GENRE_ID + " not found!");
    }

    @Test
    void shouldPassCorrectArgumentsToDaoMethodOnRemoveGenre() {
        Genre genre = new Genre();
        genre.setId(GENRE_ID);
        genre.setName(GENRE_NAME);
        BookHandlerServiceImpl service = new BookHandlerServiceImpl(bookDao, authorDao, genreDao);
        Mockito.when(bookDao.findById(BOOK_ID)).thenReturn(Optional.of(book));
        Mockito.when(genreDao.findById(GENRE_ID)).thenReturn(Optional.of(genre));
        service.removeGenre(BOOK_ID, GENRE_ID);
        Mockito.verify(book).removeGenre(genreArgumentCaptor.capture());
        Assertions.assertThat(genreArgumentCaptor.getValue()).isEqualTo(genre);
    }
}