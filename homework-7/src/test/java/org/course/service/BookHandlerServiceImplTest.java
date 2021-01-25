package org.course.service;

import liquibase.pro.packaged.U;
import org.assertj.core.api.Assertions;
import org.course.domain.Author;
import org.course.domain.Book;
import org.course.domain.Genre;
import org.course.domain.User;
import org.course.repository.AuthorRepository;
import org.course.repository.BookCommentRepository;
import org.course.repository.BookRepository;
import org.course.repository.GenreRepository;
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
    public static final int PAGE_NUMBER = 0;
    public static final int PAGE_SIZE = 3;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private AuthorRepository authorRepository;
    @Mock
    private GenreRepository genreRepository;
    @Mock
    private BookCommentRepository bookCommentRepository;
    @Mock
    private Book book;
    @Captor
    private ArgumentCaptor<Book> bookArgumentCaptor;
    @Captor
    private ArgumentCaptor<Genre> genreArgumentCaptor;

    @Test
    void shouldThrowExceptionIfAuthorNotFoundOnCreate() {
        BookHandlerServiceImpl service = new BookHandlerServiceImpl(bookRepository, authorRepository, genreRepository, bookCommentRepository);
        Mockito.when(authorRepository.findById(AUTHOR_ID)).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> service.createBook(BOOK_NAME, String.valueOf(AUTHOR_ID), String.valueOf(GENRE_ID)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Author with id " + AUTHOR_ID + " not found!");
    }

    @Test
    void shouldThrowExceptionIfGenreNotFoundOnCreate() {
        BookHandlerServiceImpl service = new BookHandlerServiceImpl(bookRepository, authorRepository, genreRepository, bookCommentRepository);
        Author author = new Author();
        author.setId(AUTHOR_ID);
        author.setName(AUTHOR_NAME);
        Mockito.when(authorRepository.findById(AUTHOR_ID)).thenReturn(Optional.of(author));
        Mockito.when(genreRepository.findById(GENRE_ID)).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> service.createBook(BOOK_NAME, String.valueOf(AUTHOR_ID), String.valueOf(GENRE_ID)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Genre with id " + GENRE_ID + " not found!");
    }

    @Test
    void shouldThrowExceptionIfIdParseErrorOnCreate() {
        BookHandlerServiceImpl service = new BookHandlerServiceImpl(bookRepository, authorRepository, genreRepository, bookCommentRepository);
        Assertions.assertThatThrownBy(() -> service.createBook(BOOK_NAME, "1a", String.valueOf(GENRE_ID)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Parse error! For input string: \"1a\"");
    }

    @Test
    void shouldPassCorrectArgumentsToDaoMethodOnCreate(){
        BookHandlerServiceImpl service = new BookHandlerServiceImpl(bookRepository, authorRepository, genreRepository, bookCommentRepository);
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
        Mockito.when(authorRepository.findById(AUTHOR_ID)).thenReturn(Optional.of(author));
        Mockito.when(genreRepository.findById(GENRE_ID)).thenReturn(Optional.of(genre));
        service.createBook(BOOK_NAME, String.valueOf(AUTHOR_ID), String.valueOf(GENRE_ID));
        Mockito.verify(bookRepository).save(bookArgumentCaptor.capture());
        Assertions.assertThat(bookArgumentCaptor.getValue()).isEqualTo(book);
        Assertions.assertThat(bookArgumentCaptor.getValue().getGenres()).isEqualTo(book.getGenres());
    }

    @Test
    void shouldThrowExceptionIfBookNotFoundOnReadById() {
        BookHandlerServiceImpl service = new BookHandlerServiceImpl(bookRepository, authorRepository, genreRepository, bookCommentRepository);
        Mockito.when(bookRepository.findWithAuthorAndGenreById(BOOK_ID)).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> service.readBook(BOOK_ID, PAGE_NUMBER, PAGE_SIZE))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Book with id " + BOOK_ID + " not found!");
    }

    @Test
    void shouldThrowExceptionIfBookNotFoundOnUpdate() {
        BookHandlerServiceImpl service = new BookHandlerServiceImpl(bookRepository, authorRepository, genreRepository, bookCommentRepository);
        Mockito.when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> service.updateBook(BOOK_ID, BOOK_NAME, AUTHOR_ID, AUTHOR_NAME))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Book with id " + BOOK_ID + " not found!");
    }

    @Test
    void shouldThrowExceptionIfAuthorNotFoundOnUpdate() {
        BookHandlerServiceImpl service = new BookHandlerServiceImpl(bookRepository, authorRepository, genreRepository, bookCommentRepository);
        Mockito.when(authorRepository.findById(AUTHOR_ID)).thenReturn(Optional.empty());
        Mockito.when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.of(new Book()));
        Assertions.assertThatThrownBy(() -> service.updateBook(BOOK_ID, BOOK_NAME, AUTHOR_ID, AUTHOR_NAME))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Author with id " + AUTHOR_ID + " not found!");
    }

    @Test
    void shouldPassCorrectArgumentsToDaoMethodOnUpdate(){
        BookHandlerServiceImpl service = new BookHandlerServiceImpl(bookRepository, authorRepository, genreRepository, bookCommentRepository);
        Author author = new Author();
        author.setId(AUTHOR_ID);
        author.setName(AUTHOR_NAME);
        Book book = new Book();
        book.setName(BOOK_NAME);
        book.setAuthor(author);
        Mockito.when(authorRepository.findById(AUTHOR_ID)).thenReturn(Optional.of(author));
        Mockito.when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.of(book));
        service.updateBook(BOOK_ID, BOOK_NAME, AUTHOR_ID, AUTHOR_NAME);
        Mockito.verify(bookRepository).save(bookArgumentCaptor.capture());
        Assertions.assertThat(bookArgumentCaptor.getValue()).isEqualTo(book);
    }

    @Test
    void shouldPassCorrectArgumentsToDaoMethodOnDelete() {
        BookHandlerServiceImpl service = new BookHandlerServiceImpl(bookRepository, authorRepository, genreRepository, bookCommentRepository);
        Book book = new Book();
        book.setId(BOOK_ID);
        Mockito.when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.of(book));
        service.deleteBook(BOOK_ID);
        Mockito.verify(bookRepository).delete(bookArgumentCaptor.capture());
        Assertions.assertThat(bookArgumentCaptor.getValue()).isEqualTo(book);
    }

    @Test
    void shouldInvokeCorrectDaoMethodOnDelete() {
        BookHandlerServiceImpl service = new BookHandlerServiceImpl(bookRepository, authorRepository, genreRepository, bookCommentRepository);
        service.deleteAllBooks();
        Mockito.verify(bookRepository, Mockito.times(1)).deleteAll();
    }

    @Test
    void shouldReturnCorrectBookCount() {
        BookHandlerServiceImpl service = new BookHandlerServiceImpl(bookRepository, authorRepository, genreRepository, bookCommentRepository);
        Mockito.when(bookRepository.count()).thenReturn(BOOK_COUNT);
        String actual = service.countBooks();
        Assertions.assertThat(actual).isEqualTo(String.valueOf(BOOK_COUNT));
    }

    @Test
    void shouldThrowExceptionIfBookNotFoundOnAddGenre() {
        BookHandlerServiceImpl service = new BookHandlerServiceImpl(bookRepository, authorRepository, genreRepository, bookCommentRepository);
        Mockito.when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> service.addGenre(BOOK_ID, GENRE_ID))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Book with id " + BOOK_ID + " not found!");
    }

    @Test
    void shouldThrowExceptionIfGenreNotFoundOnAddGenre() {
        BookHandlerServiceImpl service = new BookHandlerServiceImpl(bookRepository, authorRepository, genreRepository, bookCommentRepository);
        Mockito.when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.of(new Book()));
        Mockito.when(genreRepository.findById(GENRE_ID)).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> service.addGenre(BOOK_ID, GENRE_ID))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Genre with id " + GENRE_ID + " not found!");
    }

    @Test
    void shouldPassCorrectArgumentsToAddGenreMethod() {
        Genre genre = new Genre();
        genre.setId(GENRE_ID);
        genre.setName(GENRE_NAME);
        BookHandlerServiceImpl service = new BookHandlerServiceImpl(bookRepository, authorRepository, genreRepository, bookCommentRepository);
        Mockito.when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.of(book));
        Mockito.when(genreRepository.findById(GENRE_ID)).thenReturn(Optional.of(genre));
        service.addGenre(BOOK_ID, GENRE_ID);
        Mockito.verify(book).addGenre(genreArgumentCaptor.capture());
        Assertions.assertThat(genreArgumentCaptor.getValue()).isEqualTo(genre);
    }


    @Test
    void shouldThrowExceptionIfBookNotFoundOnRemoveGenre() {
        BookHandlerServiceImpl service = new BookHandlerServiceImpl(bookRepository, authorRepository, genreRepository, bookCommentRepository);
        Mockito.when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> service.removeGenre(BOOK_ID, GENRE_ID))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Book with id " + BOOK_ID + " not found!");
    }

    @Test
    void shouldThrowExceptionIfGenreNotFoundOnRemoveGenre() {
        BookHandlerServiceImpl service = new BookHandlerServiceImpl(bookRepository, authorRepository, genreRepository, bookCommentRepository);
        Mockito.when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.of(new Book()));
        Mockito.when(genreRepository.findById(GENRE_ID)).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> service.removeGenre(BOOK_ID, GENRE_ID))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Genre with id " + GENRE_ID + " not found!");
    }

    @Test
    void shouldPassCorrectArgumentsToDaoMethodOnRemoveGenre() {
        Genre genre = new Genre();
        genre.setId(GENRE_ID);
        genre.setName(GENRE_NAME);
        BookHandlerServiceImpl service = new BookHandlerServiceImpl(bookRepository, authorRepository, genreRepository, bookCommentRepository);
        Mockito.when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.of(book));
        Mockito.when(genreRepository.findById(GENRE_ID)).thenReturn(Optional.of(genre));
        service.removeGenre(BOOK_ID, GENRE_ID);
        Mockito.verify(book).removeGenre(genreArgumentCaptor.capture());
        Assertions.assertThat(genreArgumentCaptor.getValue()).isEqualTo(genre);
    }
}