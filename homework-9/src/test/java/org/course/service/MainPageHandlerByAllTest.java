package org.course.service;

import org.assertj.core.api.Assertions;
import org.course.components.BookRepoResolverSortBest;
import org.course.components.BookRepoResolverSortNew;
import org.course.components.BookRepoResolverSortPopular;
import org.course.components.interfaces.BookRepoResolver;
import org.course.domain.*;
import org.course.dto.attributes.MainPageAttributes;
import org.course.dto.request.MainPageRequest;
import org.course.dto.state.MainPageParams;
import org.course.repository.GenreRepository;
import org.course.service.interfaces.MainPageHandler;
import org.course.service.interfaces.PagingAndSortingHandler;
import org.course.utility.BookSort;
import org.course.utility.MainPageBehavior;
import org.course.utility.Route;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.*;

@DisplayName("Class MainPageHandlerByAll")
@ExtendWith(MockitoExtension.class)
class MainPageHandlerByAllTest {

    public static final List<Book> BOOK_PAGE_CONTENT = List.of(new Book(), new Book());
    public static final int TOTAL_BOOK_COUNT = 6;
    public static final int BOOK_PAGE_SIZE = BOOK_PAGE_CONTENT.size();
    public static final int BOOK_PAGE_COUNT = (int) Math.ceil(((double) (TOTAL_BOOK_COUNT)) / BOOK_PAGE_SIZE);
    public static final int PAGE_NUMBER = 0;
    public static final long GENRE_ID = 1L;
    public static final int NEXT_PAGE_NUMBER = 1;

    @Mock
    private GenreRepository genreRepository;

    @Mock
    private BookRepoResolverSortNew bookRepoResolverSortNew;

    @Mock
    private BookRepoResolverSortBest bookRepoResolverSortBest;

    @Mock
    private BookRepoResolverSortPopular bookRepoResolverSortPopular;

    @Mock
    Map<BookSort, BookRepoResolver> bookRepoResolverMap;

    @Mock
    private PagingAndSortingHandler pagingAndSortingHandler;

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

    @Test
    void shouldSetAttributesWhenFirstLoaded() {
        //first visit
        //Create service
        MainPageHandler handler = new MainPageHandlerByAll(pagingAndSortingHandler, genreRepository, bookRepoResolverMap);

        //SetUp expected
        MainPageParams expectedParams = new MainPageParams(MainPageBehavior.BY_ALL, null, BookSort.POPULAR, PAGE_NUMBER, BOOK_PAGE_COUNT, null);
        MainPageAttributes expected = new MainPageAttributes(expectedParams, BOOK_PAGE_CONTENT, genreList);

        //Input params
        MainPageRequest request = new MainPageRequest(MainPageBehavior.BY_ALL, null, null, BookSort.POPULAR, null);//defaults
        MainPageParams params = new MainPageParams(MainPageBehavior.BY_ALL, null, null, 0,0,null);//defaults

        //Mock section
        PageRequest pageAndSort = PageRequest.of(PAGE_NUMBER, BOOK_PAGE_SIZE);//sort omitted in mock
        PageImpl<Book> bookPage = new PageImpl<>(BOOK_PAGE_CONTENT, pageAndSort, TOTAL_BOOK_COUNT);

        Mockito.when(genreRepository.findAll()).thenReturn(genreList);
        Mockito.when(pagingAndSortingHandler
                .processPageNumber(request.getDirection(), params.getTotalPages(), params.getCurrentPage()))
                .thenReturn(PAGE_NUMBER);
        Mockito.when(pagingAndSortingHandler
                .processSort(request.getDirection(), params.getSort(), request.getSort()))
                .thenReturn(BookSort.POPULAR);
        Mockito.when(bookRepoResolverMap.get(BookSort.POPULAR)).thenReturn(bookRepoResolverSortPopular);
        Mockito.when(bookRepoResolverSortPopular.getPage(PAGE_NUMBER)).thenReturn(bookPage);

        //execute service
        MainPageAttributes actual = handler.getMainPageAttributes(request, params);

        //Assertion
        Assertions.assertThat(actual).isEqualTo(expected);

    }

    @Test
    void shouldSetAttributesOnSortRequest() {
        //sort case
        //Create service
        MainPageHandler handler = new MainPageHandlerByAll(pagingAndSortingHandler, genreRepository, bookRepoResolverMap);

        //SetUp expected
        MainPageParams expectedParams = new MainPageParams(MainPageBehavior.BY_ALL, null, BookSort.BEST, PAGE_NUMBER, BOOK_PAGE_COUNT, null);
        MainPageAttributes expected = new MainPageAttributes(expectedParams, BOOK_PAGE_CONTENT, genreList);

        //Input params
        MainPageRequest request = new MainPageRequest(MainPageBehavior.BY_ALL, null, null, BookSort.BEST, null);//sort
        MainPageParams params = new MainPageParams(MainPageBehavior.BY_GENRE, GENRE_ID, BookSort.NEW, 0, BOOK_PAGE_SIZE, null);//any values

        //Mock section
        PageRequest pageAndSort = PageRequest.of(PAGE_NUMBER, BOOK_PAGE_SIZE);//sort omitted in mock
        PageImpl<Book> bookPage = new PageImpl<>(BOOK_PAGE_CONTENT, pageAndSort, TOTAL_BOOK_COUNT);

        Mockito.when(genreRepository.findAll()).thenReturn(genreList);
        Mockito.when(pagingAndSortingHandler
                .processPageNumber(request.getDirection(), params.getTotalPages(), params.getCurrentPage()))
                .thenReturn(PAGE_NUMBER);
        Mockito.when(pagingAndSortingHandler
                .processSort(request.getDirection(), params.getSort(), request.getSort()))
                .thenReturn(BookSort.BEST);
        Mockito.when(bookRepoResolverMap.get(BookSort.BEST)).thenReturn(bookRepoResolverSortBest);
        Mockito.when(bookRepoResolverSortBest.getPage(PAGE_NUMBER)).thenReturn(bookPage);

        //execute service
        MainPageAttributes actual = handler.getMainPageAttributes(request, params);

        //Assertion
        Assertions.assertThat(actual).isEqualTo(expected);

    }

    @Test
    void shouldSetAttributesOnPagingRequest() {
        //paging case
        //Create service
        MainPageHandler handler = new MainPageHandlerByAll(pagingAndSortingHandler, genreRepository, bookRepoResolverMap);

        //SetUp expected
        MainPageParams expectedParams = new MainPageParams(MainPageBehavior.BY_ALL, null, BookSort.POPULAR, PAGE_NUMBER + 1, BOOK_PAGE_COUNT, null);
        MainPageAttributes expected = new MainPageAttributes(expectedParams, BOOK_PAGE_CONTENT, genreList);

        //Input params
        MainPageRequest request = new MainPageRequest(MainPageBehavior.BY_ALL, Route.NEXT, null, null, null);//paging
        MainPageParams params = new MainPageParams(MainPageBehavior.BY_ALL, null, BookSort.POPULAR, PAGE_NUMBER, BOOK_PAGE_SIZE, null);//sort, page

        //Mock section
        PageRequest pageAndSort = PageRequest.of(NEXT_PAGE_NUMBER, BOOK_PAGE_SIZE);//sort omitted in mock
        PageImpl<Book> bookPage = new PageImpl<>(BOOK_PAGE_CONTENT, pageAndSort, TOTAL_BOOK_COUNT);

        Mockito.when(genreRepository.findAll()).thenReturn(genreList);
        Mockito.when(pagingAndSortingHandler
                .processPageNumber(request.getDirection(), params.getTotalPages(), params.getCurrentPage()))
                .thenReturn(PAGE_NUMBER + 1);
        Mockito.when(pagingAndSortingHandler
                .processSort(request.getDirection(), params.getSort(), request.getSort()))
                .thenReturn(BookSort.POPULAR);
        Mockito.when(bookRepoResolverMap.get(BookSort.POPULAR)).thenReturn(bookRepoResolverSortPopular);
        Mockito.when(bookRepoResolverSortPopular.getPage(PAGE_NUMBER + 1)).thenReturn(bookPage);

        //execute service
        MainPageAttributes actual = handler.getMainPageAttributes(request, params);

        //Assertion
        Assertions.assertThat(actual).isEqualTo(expected);

    }

}