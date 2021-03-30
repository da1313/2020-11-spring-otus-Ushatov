package org.course.service;

import org.assertj.core.api.Assertions;
import org.course.api.attributes.BookPageAttributes;
import org.course.api.attributes.BooksPageAttributes;
import org.course.api.attributes.PagingAttributes;
import org.course.api.attributes.UpdateBookPageAttributes;
import org.course.config.AppConfig;
import org.course.domain.*;
import org.course.repository.AuthorRepository;
import org.course.repository.BookRepository;
import org.course.repository.CommentRepository;
import org.course.repository.GenreRepository;
import org.course.service.interfaces.PagesService;
import org.course.service.interfaces.PagingService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@DisplayName("Class PagesServiceImpl")
@ExtendWith(MockitoExtension.class)
class PagesServiceImplTest {

    public static final long BOOK_ID = 1L;
    public static final User USER = new User();
    public static final Author AUTHOR = new Author(1L, "A");
    public static final List<Author> AUTHORS = List.of(AUTHOR);
    public static final Set<Genre> GENRES = Set.of(new Genre(1L, "G"));
    public static final ArrayList<Genre> GENRES_LIST = new ArrayList<>(GENRES);
    public static final Book BOOK = new Book(BOOK_ID, "T", "D", LocalDateTime.now(), AUTHOR, GENRES, new Info());
    public static final List<Book> BOOK_LIST = List.of(BOOK);
    public static final List<Comment> COMMENT_LIST = List.of(new Comment(1L, "T", LocalDateTime.now(), BOOK, USER));
    public static final int TOTAL_PAGES = 2;
    public static final PagingAttributes PAGING_ATTRIBUTES = new PagingAttributes(3, List.of(1), List.of(3), 2, 3, 1);
    public static final int PAGE = 1;
    public static final int SIZE = 1;
    public static final int LEFT = 1;
    public static final int RIGHT = 1;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private GenreRepository genreRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PagingService pagingService;

    @Mock
    private AppConfig appConfig;

    @Test
    void shouldGetBooksPageAttributes() {

        PagesService service = new PagesServiceImpl(bookRepository, authorRepository, genreRepository, commentRepository,
                pagingService, appConfig);

        BooksPageAttributes attributes = new BooksPageAttributes(BOOK_LIST, PAGING_ATTRIBUTES);

        PageRequest pageRequest = PageRequest.of(PAGE, SIZE, Sort.by("id").ascending());

        PageImpl<Book> bookPage = new PageImpl<>(BOOK_LIST, pageRequest, TOTAL_PAGES);

        Mockito.when(bookRepository.findAllEager(pageRequest)).thenReturn(bookPage);
        Mockito.when(pagingService.getPageAttributes(PAGE, TOTAL_PAGES)).thenReturn(PAGING_ATTRIBUTES);
        Mockito.when(appConfig.getBookPageSize()).thenReturn(SIZE);

        BooksPageAttributes actual = service.getBooksPageAttributes(PAGE + 1);

        Assertions.assertThat(actual).isEqualTo(attributes);

    }

    @Test
    void shouldGetUpdateBookPageAttributes() {

        PagesService service = new PagesServiceImpl(bookRepository, authorRepository, genreRepository, commentRepository,
                pagingService, appConfig);

        UpdateBookPageAttributes attributes = new UpdateBookPageAttributes(AUTHORS, GENRES_LIST, BOOK);

        Mockito.when(authorRepository.findAll()).thenReturn(AUTHORS);
        Mockito.when(genreRepository.findAll()).thenReturn(GENRES_LIST);
        Mockito.when(bookRepository.findByIdEager(BOOK_ID)).thenReturn(Optional.of(BOOK));

        UpdateBookPageAttributes actual = service.getUpdateBookPageAttributes(BOOK_ID);

        Assertions.assertThat(actual).isEqualTo(attributes);

    }

    @Test
    void shouldGetCreateBookPageAttributes() {

        PagesService service = new PagesServiceImpl(bookRepository, authorRepository, genreRepository, commentRepository,
                pagingService, appConfig);

        UpdateBookPageAttributes attributes = new UpdateBookPageAttributes(AUTHORS, GENRES_LIST, null);

        Mockito.when(authorRepository.findAll()).thenReturn(AUTHORS);
        Mockito.when(genreRepository.findAll()).thenReturn(GENRES_LIST);

        UpdateBookPageAttributes actual = service.getCreateBookPageAttributes();

        Assertions.assertThat(actual).isEqualTo(attributes);

    }

    @Test
    void shouldGetBookPageAttributes() {

        PagesService service = new PagesServiceImpl(bookRepository, authorRepository, genreRepository, commentRepository,
                pagingService, appConfig);

        BookPageAttributes attributes = new BookPageAttributes(BOOK, COMMENT_LIST, PAGING_ATTRIBUTES);

        PageRequest pageRequest = PageRequest.of(PAGE, SIZE, Sort.by("time").descending());

        PageImpl<Comment> commentPage = new PageImpl<>(COMMENT_LIST, pageRequest, TOTAL_PAGES);

        Mockito.when(bookRepository.findByIdEager(BOOK_ID)).thenReturn(Optional.of(BOOK));
        Mockito.when(commentRepository.findByBook(BOOK, pageRequest)).thenReturn(commentPage);
        Mockito.when(pagingService.getPageAttributes(PAGE, TOTAL_PAGES)).thenReturn(PAGING_ATTRIBUTES);
        Mockito.when(appConfig.getCommentPageSize()).thenReturn(SIZE);

        BookPageAttributes actual = service.getBookPageAttributes(BOOK_ID, PAGE + 1);

        Assertions.assertThat(actual).isEqualTo(attributes);

    }
}