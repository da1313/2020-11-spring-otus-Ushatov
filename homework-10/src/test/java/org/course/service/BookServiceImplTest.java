package org.course.service;

import org.assertj.core.api.Assertions;
import org.course.api.requests.BookListRequest;
import org.course.api.requests.BookRequest;
import org.course.api.responces.BookInfoResponse;
import org.course.api.responces.BookListResponse;
import org.course.domain.*;
import org.course.exceptions.EntityNotFoundException;
import org.course.api.pojo.BookCount;
import org.course.api.pojo.BookShort;
import org.course.repository.AuthorRepository;
import org.course.repository.BookRepository;
import org.course.repository.GenreRepository;
import org.course.service.interfaces.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@DisplayName("Class BookServiceImpl")
@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    public static final String GENRE_ID = "id";
    public static final String QUERY = "query";
    public static final String BOOK_ID = "id";
    public static final String TITLE = "t";
    public static final String AUTHOR_ID = "a_id";
    public static final String GENRE_ID_1 = "g1";
    public static final String GENRE_ID_2 = "g2";
    public static final List<String> GENRES_IDS = List.of(GENRE_ID_1, GENRE_ID_2);
    public static final Author SOME_AUTHOR = new Author("x", "y");
    public static final List<Genre> SOME_GENRES = List.of(new Genre("x", "y"), new Genre("a", "b"));
    public static int TOTAL_PAGES = 0;
    private final List<BookShort> FAKE_BOOKS = new ArrayList<>();
    public static Book FAKE_BOOK;
    public static final int PAGE_NUMBER = 0;
    public static final int PAGE_SIZE = 1;
    public static final BookSort BOOK_SORT = BookSort.NEW;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private GenreRepository genreRepository;

    @Mock
    private AuthorRepository authorRepository;



    @BeforeEach
    void init(){

        FAKE_BOOKS.clear();

        BookShort b1 = new BookShort();
        b1.setId("i1");
        BookShort b2 = new BookShort();
        b1.setId("i2");

        FAKE_BOOKS.add(b1);
        FAKE_BOOKS.add(b2);

        TOTAL_PAGES = (int) Math.ceil(((double) FAKE_BOOKS.size()) / PAGE_SIZE);

        FAKE_BOOK = new Book(BOOK_ID,
                "T",
                LocalDateTime.now(),
                new Author("id", "A"),
                List.of(new Genre("id", "G")),
                new Info());
    }

    @Test
    void shouldGetBooks() {

        BookService service = new BookServiceImpl(bookRepository, authorRepository, genreRepository);

        BookListRequest request = new BookListRequest(PAGE_NUMBER, PAGE_SIZE, BOOK_SORT);

        PageRequest pageRequest = PageRequest.of(PAGE_NUMBER, PAGE_SIZE, Sort.by(BOOK_SORT.getField()).descending());

        Mockito.when(bookRepository.count()).thenReturn((long) FAKE_BOOKS.size());

        Mockito.when(bookRepository.findAllBookShortAuto(pageRequest)).thenReturn(FAKE_BOOKS);

        BookListResponse expected = new BookListResponse(FAKE_BOOKS, TOTAL_PAGES);

        BookListResponse actual = service.getBooks(request, null, null);

        Assertions.assertThat(actual).isEqualTo(expected);

    }

    @Test
    void shouldGetBooksByGenre() {

        BookService service = new BookServiceImpl(bookRepository, authorRepository, genreRepository);

        BookListRequest request = new BookListRequest(PAGE_NUMBER, PAGE_SIZE, BOOK_SORT);

        PageRequest pageRequest = PageRequest.of(PAGE_NUMBER, PAGE_SIZE, Sort.by(BOOK_SORT.getField()).descending());

        Mockito.when(bookRepository.findCountByGenres(GENRE_ID)).thenReturn(new BookCount(FAKE_BOOKS.size()));

        Mockito.when(bookRepository.findAllBookShortByGenre(GENRE_ID, pageRequest)).thenReturn(FAKE_BOOKS);

        BookListResponse expected = new BookListResponse(FAKE_BOOKS, TOTAL_PAGES);

        BookListResponse actual = service.getBooks(request, GENRE_ID, null);

        Assertions.assertThat(actual).isEqualTo(expected);

    }

    @Test
    void shouldGetBooksByQuery() {

        BookService service = new BookServiceImpl(bookRepository, authorRepository, genreRepository);

        BookListRequest request = new BookListRequest(PAGE_NUMBER, PAGE_SIZE, BOOK_SORT);

        PageRequest pageRequest = PageRequest.of(PAGE_NUMBER, PAGE_SIZE, Sort.by(BOOK_SORT.getField()).descending());

        Mockito.when(bookRepository.findCountByQuery(QUERY)).thenReturn(new BookCount(FAKE_BOOKS.size()));

        Mockito.when(bookRepository.findAllBookShortByQuery(QUERY, pageRequest)).thenReturn(FAKE_BOOKS);

        BookListResponse expected = new BookListResponse(FAKE_BOOKS, TOTAL_PAGES);

        BookListResponse actual = service.getBooks(request, null, QUERY);

        Assertions.assertThat(actual).isEqualTo(expected);

    }

    @Test
    void shouldGetBookById() {

        BookService service = new BookServiceImpl(bookRepository, authorRepository, genreRepository);

        Mockito.when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.of(FAKE_BOOK));

        BookInfoResponse expected = new BookInfoResponse(FAKE_BOOK.getId(),
                FAKE_BOOK.getTitle(),
                FAKE_BOOK.getTime(),
                FAKE_BOOK.getAuthor(),
                FAKE_BOOK.getGenres(),
                List.of(0,0,0,0,0),
                FAKE_BOOK.getInfo().getAvgScore(),
                FAKE_BOOK.getInfo().getCommentCount());

        BookInfoResponse actual = service.getBookById(BOOK_ID);

        Assertions.assertThat(actual).isEqualTo(expected);

    }

    @Test
    void shouldGThrowExceptionIfBookNotFound() {

        BookService service = new BookServiceImpl(bookRepository, authorRepository, genreRepository);

        Mockito.when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> service.getBookById(BOOK_ID))
                .withMessage("Book with id " + BOOK_ID + " not found");

    }

    @Test
    void shouldCreateBook() {

        BookService service = new BookServiceImpl(bookRepository, authorRepository, genreRepository);

        BookRequest request = new BookRequest(TITLE, AUTHOR_ID, GENRES_IDS);

        Mockito.when(authorRepository.findById(AUTHOR_ID)).thenReturn(Optional.of(SOME_AUTHOR));

        Mockito.when(genreRepository.findByIdIn(GENRES_IDS)).thenReturn(SOME_GENRES);

        Book expected = new Book(null, TITLE, null, SOME_AUTHOR, SOME_GENRES, new Info());

        service.createBook(request);

        ArgumentCaptor<Book> bookArgumentCaptor = ArgumentCaptor.forClass(Book.class);

        Mockito.verify(bookRepository).save(bookArgumentCaptor.capture());

        Book actual = bookArgumentCaptor.getValue();

        actual.setTime(expected.getTime());//cant predict time since its auto generated

        Assertions.assertThat(actual).isEqualTo(expected);

    }

    @Test
    void shouldUpdateBook() {

        BookService service = new BookServiceImpl(bookRepository, authorRepository, genreRepository);

        BookRequest request = new BookRequest(TITLE, AUTHOR_ID, GENRES_IDS);

        Mockito.when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.of(FAKE_BOOK));

        Mockito.when(authorRepository.findById(AUTHOR_ID)).thenReturn(Optional.of(SOME_AUTHOR));

        Mockito.when(genreRepository.findByIdIn(GENRES_IDS)).thenReturn(SOME_GENRES);

        Book expected = new Book(FAKE_BOOK.getId(), TITLE, FAKE_BOOK.getTime(), SOME_AUTHOR, SOME_GENRES, FAKE_BOOK.getInfo());

        service.updateBook(BOOK_ID, request);

        ArgumentCaptor<Book> bookArgumentCaptor = ArgumentCaptor.forClass(Book.class);

        Mockito.verify(bookRepository).save(bookArgumentCaptor.capture());

        Book actual = bookArgumentCaptor.getValue();

        Assertions.assertThat(actual).isEqualTo(expected);

    }

    @Test
    void shouldDeleteBook() {

        BookService service = new BookServiceImpl(bookRepository, authorRepository, genreRepository);

        Mockito.when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.of(FAKE_BOOK));

        service.deleteBook(BOOK_ID);

        ArgumentCaptor<Book> bookArgumentCaptor = ArgumentCaptor.forClass(Book.class);

        Mockito.verify(bookRepository).delete(bookArgumentCaptor.capture());

        Book actual = bookArgumentCaptor.getValue();

        Assertions.assertThat(actual).isEqualTo(FAKE_BOOK);

    }
}