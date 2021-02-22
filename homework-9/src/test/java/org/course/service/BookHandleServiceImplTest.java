package org.course.service;

import org.assertj.core.api.Assertions;
import org.course.configurations.AppConfig;
import org.course.domain.*;
import org.course.dto.attributes.BookPageAttributes;
import org.course.dto.attributes.MainPageAttributes;
import org.course.dto.state.BookPageParams;
import org.course.dto.state.MainPageParams;
import org.course.repository.AuthorRepository;
import org.course.repository.BookRepository;
import org.course.repository.CommentRepository;
import org.course.repository.GenreRepository;
import org.course.service.interfaces.BookHandleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.*;

@DisplayName("Class BookHandleServiceImpl")
@ExtendWith(MockitoExtension.class)
class BookHandleServiceImplTest {

    public static final String SEARCH_QUERY = "QUERY";
    public static final int ANY_GENRE = 0;
    public static final String ANY_SORT = "new";
    public static final int ANY_CURRENT_PAGE = 0;
    public static final boolean ANY_SEARCH_STATUS = false;
    public static final String ANY_QUERY = "";
    public static final int NEXT_PAGE_NO_PAGING_REQUEST = 0;
    public static final int EMPTY_GENRE = 0;
    public static final long GENRE_ID = 1;
    public static final int EXPECTED_NEXT_PAGE_NO_PAGE_REQUEST = 0;
    public static final int NEXT_PAGE_LOWER_THAN_ZERO = -1;
    public static final int EXPECTED_ZERO_PAGE = 0;

    public static final List<Book> BOOK_PAGE_CONTENT = List.of(new Book(), new Book());
    public static final int TOTAL_BOOK_COUNT = 7;
    public static final int BOOK_PAGE_SIZE = BOOK_PAGE_CONTENT.size();
    public static final int BOOK_PAGE_COUNT = (int) Math.ceil(((double) (TOTAL_BOOK_COUNT)) / BOOK_PAGE_SIZE);

    public static final List<Comment> COMMENT_PAGE_CONTENT = List.of(new Comment(), new Comment());
    public static final int TOTAL_COMMENT_COUNT = 7;
    public static final int COMMENT_PAGE_SIZE = COMMENT_PAGE_CONTENT.size();
    public static final int COMMENT_PAGE_COUNT = (int) Math.ceil(((double) (TOTAL_COMMENT_COUNT)) / COMMENT_PAGE_SIZE);
    public static final int BOOK_ID = 1;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private GenreRepository genreRepository;

    @Mock
    private AppConfig appConfig;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Captor
    private ArgumentCaptor<Pageable> pageableArgumentCaptor;

    private final List<Book> bookList = new ArrayList<>();
    private final List<Author> authorList = new ArrayList<>() ;
    private final List<Genre> genreList = new ArrayList<>();
    private final List<Comment> commentList = new ArrayList<>();
    private final List<Score> scoreList = new ArrayList<>();

    @BeforeEach
    private void initData(){

        User user = new User();

        for (long i = 0; i < 3; i++) {
            Author author = new Author(i + 1, "A" + i, new ArrayList<>());
            authorList.add(author);
        }
        for (long i = 0; i < 3; i++) {
            Genre genre = new Genre(i + 1, "G" + i, new HashSet<>());
            genreList.add(genre);
        }

        bookList.add(new Book(1, "B1", "D1", authorList.get(0), LocalDateTime.now(),new BookInfo(), Set.of(genreList.get(0), genreList.get(1)), new ArrayList<>(), new ArrayList<>()));
        bookList.add(new Book(2, "B2", "D2", authorList.get(1), LocalDateTime.now(),new BookInfo(), Set.of(genreList.get(0)),  new ArrayList<>(), new ArrayList<>()));
        bookList.add(new Book(3, "B3", "D3", authorList.get(0), LocalDateTime.now(),new BookInfo(), Set.of(genreList.get(0), genreList.get(1), genreList.get(2)),  new ArrayList<>(), new ArrayList<>()));
        bookList.add(new Book(4, "B4", "D4", authorList.get(2), LocalDateTime.now(),new BookInfo(), Set.of(genreList.get(1)),  new ArrayList<>(), new ArrayList<>()));
        bookList.add(new Book(5, "B5", "D5", authorList.get(1), LocalDateTime.now(),new BookInfo(), Set.of(genreList.get(1), genreList.get(2)),  new ArrayList<>(), new ArrayList<>()));
        bookList.add(new Book(6, "B6", "D6", authorList.get(2), LocalDateTime.now(),new BookInfo(), Set.of(genreList.get(2)),  new ArrayList<>(), new ArrayList<>()));


        authorList.forEach(a -> bookList.stream().filter(b -> b.getAuthor().equals(a)).forEach(b -> a.getBooks().add(b)));
        genreList.forEach(g -> bookList.stream().filter(b -> b.getGenres().contains(g)).forEach(b -> g.getBooks().add(b)));

        commentList.add(new Comment(1, "C1", bookList.get(0), user));
        commentList.add(new Comment(2, "C2", bookList.get(0), user));
        commentList.add(new Comment(3, "C3", bookList.get(1), user));
        commentList.add(new Comment(4, "C4", bookList.get(1), user));
        commentList.add(new Comment(5, "C5", bookList.get(1), user));
        commentList.add(new Comment(6, "C6", bookList.get(2), user));
        bookList.forEach(b -> commentList.stream().filter(c -> c.getBook().equals(b)).forEach(c -> b.getComments().add(c)));

        scoreList.add(new Score(1, 1, user, bookList.get(0)));
        scoreList.add(new Score(2, 2, user, bookList.get(0)));
        scoreList.add(new Score(3, 3, user, bookList.get(0)));
        scoreList.add(new Score(4, 3, user, bookList.get(1)));
        scoreList.add(new Score(5, 4, user, bookList.get(1)));
        scoreList.add(new Score(6, 3, user, bookList.get(2)));
        scoreList.add(new Score(7, 5, user, bookList.get(2)));
        scoreList.add(new Score(8, 1, user, bookList.get(2)));
        bookList.forEach(b -> scoreList.stream().filter(s -> s.getBook().equals(b)).forEach(s -> b.getBookScores().add(s)));

        bookList.forEach(b -> b.getBookInfo().setScoreOneCount((int) b.getBookScores().stream().filter(s -> s.getScore() == 1).count()));
        bookList.forEach(b -> b.getBookInfo().setScoreTwoCount((int) b.getBookScores().stream().filter(s -> s.getScore() == 2).count()));
        bookList.forEach(b -> b.getBookInfo().setScoreThreeCount((int) b.getBookScores().stream().filter(s -> s.getScore() == 3).count()));
        bookList.forEach(b -> b.getBookInfo().setScoreFourCount((int) b.getBookScores().stream().filter(s -> s.getScore() == 4).count()));
        bookList.forEach(b -> b.getBookInfo().setScoreFiveCount((int) b.getBookScores().stream().filter(s -> s.getScore() == 5).count()));



    }

    @DisplayName("Метод getMainPageAttributes должен при MainPageParams previousParams равном null загрузить все книги с сортировкой по времени")
    @Test
    void shouldFindBooksSortedByTimeAndSetAttributesWhenPageFirstTimeLoaded() {

        BookHandleService service = new BookHandleServiceImpl(bookRepository, genreRepository, appConfig, commentRepository, authorRepository);

        PageRequest pageAndSort = PageRequest.of(EXPECTED_NEXT_PAGE_NO_PAGE_REQUEST, BOOK_PAGE_SIZE, Sort.by("time").descending());

        PageImpl<Book> bookPage = new PageImpl<>(BOOK_PAGE_CONTENT, pageAndSort, TOTAL_BOOK_COUNT);

        MainPageParams expectedParams = new MainPageParams(0, "new", EXPECTED_NEXT_PAGE_NO_PAGE_REQUEST, BOOK_PAGE_COUNT, false, "");

        MainPageAttributes expectedAttributes = new MainPageAttributes(expectedParams, bookPage.toList(), genreList);

        Mockito.when(appConfig.getBookPageCount()).thenReturn(BOOK_PAGE_SIZE);

        Mockito.when(bookRepository.findAllEager(pageAndSort)).thenReturn(bookPage);

        Mockito.when(genreRepository.findAll()).thenReturn(genreList);

        MainPageAttributes actual = service
                .getMainPageAttributes(ANY_GENRE, ANY_SORT, ANY_CURRENT_PAGE, ANY_SEARCH_STATUS, ANY_QUERY, null);

        Mockito.verify(bookRepository, Mockito.times(1)).findAllEager(pageableArgumentCaptor.capture());

        Mockito.verify(genreRepository, Mockito.times(1)).findAll();

        Assertions.assertThat(actual).isEqualTo(expectedAttributes);

    }

    @DisplayName("Метод getMainPageAttributes должен при поисковом запросе загрузить все книги по поисковому слову")
    @Test
    void shouldFindBooksBySearchQueryIfIsSearchIsTrue() {
        BookHandleService service = new BookHandleServiceImpl(bookRepository, genreRepository, appConfig, commentRepository, authorRepository);

        PageRequest pageRequest = PageRequest.of(EXPECTED_NEXT_PAGE_NO_PAGE_REQUEST, BOOK_PAGE_SIZE);

        PageImpl<Book> bookPage = new PageImpl<>(BOOK_PAGE_CONTENT, pageRequest, TOTAL_BOOK_COUNT);

        MainPageParams expectedParams = new MainPageParams(0, "new", EXPECTED_NEXT_PAGE_NO_PAGE_REQUEST, BOOK_PAGE_COUNT, true, SEARCH_QUERY);

        MainPageAttributes expectedAttributes = new MainPageAttributes(expectedParams, bookPage.toList(), genreList);

        Mockito.when(appConfig.getBookPageCount()).thenReturn(BOOK_PAGE_SIZE);

        Mockito.when(bookRepository.findAllByQuery(SEARCH_QUERY, pageRequest)).thenReturn(bookPage);

        Mockito.when(genreRepository.findAll()).thenReturn(genreList);

        MainPageParams previousParams = new MainPageParams(ANY_GENRE, ANY_SORT, ANY_CURRENT_PAGE, BOOK_PAGE_COUNT, true, SEARCH_QUERY);

        MainPageAttributes actual = service
                .getMainPageAttributes(ANY_GENRE, ANY_SORT, NEXT_PAGE_NO_PAGING_REQUEST, true, SEARCH_QUERY, previousParams);

        Mockito.verify(bookRepository, Mockito.times(1))
                .findAllByQuery(ArgumentCaptor.forClass(String.class).capture(), pageableArgumentCaptor.capture());

        Mockito.verify(genreRepository, Mockito.times(1)).findAll();

        Assertions.assertThat(actual).isEqualTo(expectedAttributes);

    }

    @DisplayName("Метод getMainPageAttributes должен при не указанном жанре (равном 0) загрузить все книги с указанной сортировкой")
    @Test
    void shouldFindBooksIfGenreIsNotSpecified() {

        BookHandleService service = new BookHandleServiceImpl(bookRepository, genreRepository, appConfig, commentRepository, authorRepository);

        PageRequest pageRequest = PageRequest.of(EXPECTED_NEXT_PAGE_NO_PAGE_REQUEST, BOOK_PAGE_SIZE, Sort.by("time").descending());

        PageImpl<Book> bookPage = new PageImpl<>(BOOK_PAGE_CONTENT, pageRequest, TOTAL_BOOK_COUNT);

        MainPageParams expectedParams = new MainPageParams(0, "new", EXPECTED_NEXT_PAGE_NO_PAGE_REQUEST, bookPage.getTotalPages(), false, "");

        MainPageAttributes expectedAttributes = new MainPageAttributes(expectedParams, bookPage.toList(), genreList);

        Mockito.when(appConfig.getBookPageCount()).thenReturn(BOOK_PAGE_SIZE);

        Mockito.when(bookRepository.findAllEager(pageRequest)).thenReturn(bookPage);

        Mockito.when(genreRepository.findAll()).thenReturn(genreList);

        MainPageParams previousParams = new MainPageParams(ANY_GENRE, ANY_SORT, ANY_CURRENT_PAGE, BOOK_PAGE_COUNT, false, "");

        MainPageAttributes actual = service
                .getMainPageAttributes(EMPTY_GENRE, "new", NEXT_PAGE_NO_PAGING_REQUEST, false, ANY_QUERY, previousParams);

        Mockito.verify(bookRepository, Mockito.times(1))
                .findAllEager(pageableArgumentCaptor.capture());

        Mockito.verify(genreRepository, Mockito.times(1)).findAll();

        Assertions.assertThat(actual).isEqualTo(expectedAttributes);

    }

    @DisplayName("Метод getMainPageAttributes должен при указанном жанре загрузить все книги с этим жанром и сортировкой")
    @Test
    void shouldFindBooksByGenre() {

        BookHandleService service = new BookHandleServiceImpl(bookRepository, genreRepository, appConfig, commentRepository, authorRepository);

        Genre genre = genreList.get(0);

        PageRequest pageRequest = PageRequest.of(EXPECTED_NEXT_PAGE_NO_PAGE_REQUEST, BOOK_PAGE_SIZE, Sort.by("time").descending());

        PageImpl<Book> bookPage = new PageImpl<>(BOOK_PAGE_CONTENT, pageRequest, TOTAL_BOOK_COUNT);

        MainPageParams expectedParams = new MainPageParams(GENRE_ID, "new", EXPECTED_NEXT_PAGE_NO_PAGE_REQUEST, bookPage.getTotalPages(), false, "");

        MainPageAttributes expectedAttributes = new MainPageAttributes(expectedParams, bookPage.toList(), genreList);

        Mockito.when(appConfig.getBookPageCount()).thenReturn(BOOK_PAGE_SIZE);

        Mockito.when(bookRepository.findAllByGenre(genre, pageRequest)).thenReturn(bookPage);

        Mockito.when(genreRepository.findById(GENRE_ID)).thenReturn(Optional.of(genre));

        Mockito.when(genreRepository.findAll()).thenReturn(genreList);

        MainPageParams previousParams = new MainPageParams(ANY_GENRE, ANY_SORT, ANY_CURRENT_PAGE, TOTAL_BOOK_COUNT, false, "");

        MainPageAttributes actual = service
                .getMainPageAttributes(GENRE_ID, "new", NEXT_PAGE_NO_PAGING_REQUEST, false, ANY_QUERY, previousParams);

        Mockito.verify(bookRepository, Mockito.times(1))
                .findAllByGenre(Mockito.eq(genre), pageableArgumentCaptor.capture());

        Mockito.verify(genreRepository, Mockito.times(1)).findAll();

        Assertions.assertThat(actual).isEqualTo(expectedAttributes);

    }

    @DisplayName("Метод getMainPageAttributes должен не давать следующей старице превысить максимальное количество")
    @Test
    void shouldUpdateCurrentPageNumberWhenRequestedPageHigherWhenTotalPagesNoEmptyGenreCase() {

        BookHandleService service = new BookHandleServiceImpl(bookRepository, genreRepository, appConfig, commentRepository, authorRepository);

        Genre genre = genreList.get(0);

        PageRequest pageRequest = PageRequest.of(BOOK_PAGE_COUNT - 1, BOOK_PAGE_SIZE, Sort.by("time").descending());

        PageImpl<Book> bookPage = new PageImpl<>(BOOK_PAGE_CONTENT, pageRequest, TOTAL_BOOK_COUNT);

        Mockito.when(appConfig.getBookPageCount()).thenReturn(BOOK_PAGE_SIZE);

        Mockito.when(bookRepository.findAllByGenre(genre, pageRequest)).thenReturn(bookPage);

        Mockito.when(genreRepository.findById(GENRE_ID)).thenReturn(Optional.of(genre));

        Mockito.when(genreRepository.findAll()).thenReturn(genreList);

        MainPageParams expectedParams = new MainPageParams(GENRE_ID, "new", BOOK_PAGE_COUNT - 1, BOOK_PAGE_COUNT, false, "");

        MainPageAttributes expectedAttributes = new MainPageAttributes(expectedParams, bookPage.toList(), genreList);

        MainPageParams previousParams = new MainPageParams(ANY_GENRE, ANY_SORT, ANY_CURRENT_PAGE, BOOK_PAGE_COUNT, false, "");

        MainPageAttributes actual = service
                .getMainPageAttributes(genre.getId(), "new", BOOK_PAGE_COUNT, false, ANY_QUERY, previousParams);

        Mockito.verify(bookRepository, Mockito.times(1))
                .findAllByGenre(Mockito.eq(genre), pageableArgumentCaptor.capture());

        Mockito.verify(genreRepository, Mockito.times(1)).findAll();

        Assertions.assertThat(actual).isEqualTo(expectedAttributes);

    }

    @DisplayName("Метод getMainPageAttributes должен не давать следующей старице превысить минимальное количество количество")
    @Test
    void shouldUpdateCurrentPageNumberWhenRequestedPageNumberLowerThanZeroNoEmptyGenreCase() {

        BookHandleService service = new BookHandleServiceImpl(bookRepository, genreRepository, appConfig, commentRepository, authorRepository);

        Genre genre = genreList.get(0);

        PageRequest pageRequest = PageRequest.of(EXPECTED_ZERO_PAGE, BOOK_PAGE_SIZE, Sort.by("time").descending());

        PageImpl<Book> bookPage = new PageImpl<>(BOOK_PAGE_CONTENT, pageRequest, TOTAL_BOOK_COUNT);

        Mockito.when(appConfig.getBookPageCount()).thenReturn(BOOK_PAGE_SIZE);

        Mockito.when(bookRepository.findAllByGenre(genre, pageRequest)).thenReturn(bookPage);

        Mockito.when(genreRepository.findById(GENRE_ID)).thenReturn(Optional.of(genre));

        Mockito.when(genreRepository.findAll()).thenReturn(genreList);

        MainPageParams expectedParams = new MainPageParams(GENRE_ID, "new", EXPECTED_ZERO_PAGE, BOOK_PAGE_COUNT, false, "");

        MainPageAttributes expectedAttributes = new MainPageAttributes(expectedParams, bookPage.toList(), genreList);

        MainPageParams previousParams = new MainPageParams(ANY_GENRE, ANY_SORT, ANY_CURRENT_PAGE, BOOK_PAGE_COUNT, false, "");

        MainPageAttributes actual = service
                .getMainPageAttributes(GENRE_ID, "new", NEXT_PAGE_LOWER_THAN_ZERO, false, ANY_QUERY, previousParams);

        Mockito.verify(bookRepository, Mockito.times(1))
                .findAllByGenre(Mockito.eq(genre), pageableArgumentCaptor.capture());

        Mockito.verify(genreRepository, Mockito.times(1)).findAll();

        Assertions.assertThat(actual).isEqualTo(expectedAttributes);

    }

    @DisplayName("Метод getMainPageAttributes должен выбирать правильный метод репозитория")
    @Test
    void shouldChooseCorrectRepositoryMethodDependingOnSpecifiedSortValueNonEmptyGenreAndPopularSortCase() {

        BookHandleService service = new BookHandleServiceImpl(bookRepository, genreRepository, appConfig, commentRepository, authorRepository);

        Genre genre = genreList.get(0);

        PageRequest pageRequest = PageRequest.of(NEXT_PAGE_NO_PAGING_REQUEST, BOOK_PAGE_SIZE, Sort.by("bookInfo.commentCount").descending());

        PageImpl<Book> bookPage = new PageImpl<>(BOOK_PAGE_CONTENT, pageRequest, TOTAL_BOOK_COUNT);

        Mockito.when(appConfig.getBookPageCount()).thenReturn(BOOK_PAGE_SIZE);

        Mockito.when(bookRepository.findAllByGenre(genre, pageRequest)).thenReturn(bookPage);

        Mockito.when(genreRepository.findById(GENRE_ID)).thenReturn(Optional.of(genre));

        Mockito.when(genreRepository.findAll()).thenReturn(genreList);

        MainPageParams expectedParams = new MainPageParams(GENRE_ID, "popular", NEXT_PAGE_NO_PAGING_REQUEST, bookPage.getTotalPages(), false, "");

        MainPageAttributes expectedAttributes = new MainPageAttributes(expectedParams, bookPage.toList(), genreList);

        MainPageParams previousParams = new MainPageParams(ANY_GENRE, ANY_SORT, ANY_CURRENT_PAGE, bookPage.getTotalPages(), false, "");

        MainPageAttributes actual = service
                .getMainPageAttributes(GENRE_ID, "popular", NEXT_PAGE_NO_PAGING_REQUEST, false, ANY_QUERY, previousParams);

        Mockito.verify(bookRepository, Mockito.times(1))
                .findAllByGenre(Mockito.eq(genre), pageableArgumentCaptor.capture());

        Mockito.verify(genreRepository, Mockito.times(1)).findAll();

        Assertions.assertThat(actual).isEqualTo(expectedAttributes);

    }

    @DisplayName("Метод BookPageAttributes должен загружать книгу по id и ее комментарии")
    @Test
    void shouldFindBookById() {

        BookHandleService service = new BookHandleServiceImpl(bookRepository, genreRepository, appConfig, commentRepository, authorRepository);

        Book book = bookList.get(0);

        PageRequest pageRequest = PageRequest.of(0, COMMENT_PAGE_SIZE);

        PageImpl<Comment> commentPage = new PageImpl<>(COMMENT_PAGE_CONTENT, pageRequest, TOTAL_COMMENT_COUNT);

        Mockito.when(appConfig.getCommentPageCount()).thenReturn(COMMENT_PAGE_SIZE);

        Mockito.when(bookRepository.findByIdEager(BOOK_ID)).thenReturn(Optional.of(book));

        Mockito.when(commentRepository.findByBook(book, pageRequest)).thenReturn(commentPage);

        BookPageParams expectedParams = new BookPageParams(null, 0, COMMENT_PAGE_COUNT);

        BookPageAttributes expected = new BookPageAttributes(book, commentPage.toList(), expectedParams);

        BookPageAttributes actual = service.getBookPageAttributes(BOOK_ID, 0, null);

        Mockito.verify(bookRepository, Mockito.times(1)).findByIdEager(BOOK_ID);

        Mockito.verify(commentRepository, Mockito.times(1)).findByBook(book, pageRequest);

        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("Метод BookPageAttributes должен загружать книгу по id и ее комментарии номер страницы должен не выходить за максимум")
    @Test
    void shouldFindBookByIdAndPageNumberShouldNotBeOverThanMaximum() {

        BookHandleService service = new BookHandleServiceImpl(bookRepository, genreRepository, appConfig, commentRepository, authorRepository);

        Book book = bookList.get(0);

        PageRequest pageRequest = PageRequest.of(COMMENT_PAGE_COUNT - 1, COMMENT_PAGE_SIZE);

        PageImpl<Comment> commentPage = new PageImpl<>(COMMENT_PAGE_CONTENT, pageRequest, TOTAL_COMMENT_COUNT);

        Mockito.when(appConfig.getCommentPageCount()).thenReturn(COMMENT_PAGE_SIZE);

        Mockito.when(bookRepository.findByIdEager(BOOK_ID)).thenReturn(Optional.of(book));

        Mockito.when(commentRepository.findByBook(book, pageRequest)).thenReturn(commentPage);

        BookPageParams previousParams = new BookPageParams(null, ANY_CURRENT_PAGE, COMMENT_PAGE_COUNT);

        BookPageParams expectedParams = new BookPageParams(null, COMMENT_PAGE_COUNT - 1, COMMENT_PAGE_COUNT);

        BookPageAttributes expected = new BookPageAttributes(book, commentPage.toList(), expectedParams);

        BookPageAttributes actual = service.getBookPageAttributes(BOOK_ID, COMMENT_PAGE_COUNT, previousParams);

        Mockito.verify(bookRepository, Mockito.times(1)).findByIdEager(BOOK_ID);

        Mockito.verify(commentRepository, Mockito.times(1)).findByBook(book, pageRequest);

        Assertions.assertThat(actual).isEqualTo(expected);
    }
}