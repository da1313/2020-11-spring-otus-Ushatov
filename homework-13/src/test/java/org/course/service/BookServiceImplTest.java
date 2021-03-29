package org.course.service;

import org.assertj.core.api.Assertions;
import org.course.api.request.BookRequest;
import org.course.domain.Author;
import org.course.domain.Book;
import org.course.domain.Genre;
import org.course.domain.Info;
import org.course.repository.AuthorRepository;
import org.course.repository.BookRepository;
import org.course.repository.GenreRepository;
import org.course.service.interfaces.BookService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Class BookServiceImpl")
@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    public static final long BOOK_ID = 1L;
    public static final long AUTHOR_ID = 1L;
    public static final List<Long> GENRE_IDS = List.of(1L);
    public static final String DESCRIPTION = "D";
    public static final String TITLE = "T";
    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private GenreRepository genreRepository;

    @Test
    void shouldDeleteBook() {

        Book book = new Book();

        BookService bookService = new BookServiceImpl(bookRepository, authorRepository, genreRepository);

        Mockito.when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.of(book));

        bookService.deleteBook(BOOK_ID);

        Mockito.verify(bookRepository).delete(book);

    }

    @Test
    void shouldUpdateBook() {

        BookService bookService = new BookServiceImpl(bookRepository, authorRepository, genreRepository);

        Book book = new Book();

        Author author = new Author();

        Set<Genre> genreList = Set.of(new Genre());

        Book expected = new Book(book.getId(), TITLE, DESCRIPTION, book.getTime(), author, genreList, book.getInfo());

        BookRequest request = new BookRequest(TITLE, GENRE_IDS, AUTHOR_ID, DESCRIPTION);

        Mockito.when(bookRepository.findByIdEager(BOOK_ID)).thenReturn(Optional.of(book));
        Mockito.when(authorRepository.findById(AUTHOR_ID)).thenReturn(Optional.of(author));
        Mockito.when(genreRepository.findByIds(GENRE_IDS)).thenReturn(genreList);

        bookService.updateBook(BOOK_ID, request);

        ArgumentCaptor<Book> bookArgumentCaptor = ArgumentCaptor.forClass(Book.class);

        Mockito.verify(bookRepository).save(bookArgumentCaptor.capture());

        Book actual = bookArgumentCaptor.getValue();

        Assertions.assertThat(actual).isEqualTo(expected);

    }

    @Test
    void shouldCreateBook() {

        BookService bookService = new BookServiceImpl(bookRepository, authorRepository, genreRepository);

        Author author = new Author();

        Set<Genre> genreList = Set.of(new Genre());

        Book expected = new Book(0, TITLE, DESCRIPTION, null, author, genreList, new Info());

        BookRequest request = new BookRequest(TITLE, GENRE_IDS, AUTHOR_ID, DESCRIPTION);

        Mockito.when(authorRepository.findById(AUTHOR_ID)).thenReturn(Optional.of(author));
        Mockito.when(genreRepository.findByIds(GENRE_IDS)).thenReturn(genreList);

        bookService.createBook(request);

        ArgumentCaptor<Book> bookArgumentCaptor = ArgumentCaptor.forClass(Book.class);

        Mockito.verify(bookRepository).save(bookArgumentCaptor.capture());

        Book actual = bookArgumentCaptor.getValue();

        expected.setTime(actual.getTime());

        Assertions.assertThat(actual).isEqualTo(expected);

    }
}