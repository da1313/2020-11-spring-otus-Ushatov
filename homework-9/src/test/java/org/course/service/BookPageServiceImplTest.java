package org.course.service;

import org.assertj.core.api.Assertions;
import org.course.configurations.AppConfig;
import org.course.domain.*;
import org.course.dto.attributes.BookPageAttributes;
import org.course.dto.state.BookPageParams;
import org.course.repository.BookRepository;
import org.course.repository.CommentRepository;
import org.course.service.interfaces.BookPageService;
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

@DisplayName("Class BookPageServiceImpl")
@ExtendWith(MockitoExtension.class)
class BookPageServiceImplTest {

    public static final int ANY_CURRENT_PAGE = 0;
    public static final List<Comment> COMMENT_PAGE_CONTENT = List.of(new Comment(), new Comment());
    public static final int TOTAL_COMMENT_COUNT = 7;
    public static final int COMMENT_PAGE_SIZE = COMMENT_PAGE_CONTENT.size();
    public static final int COMMENT_PAGE_COUNT = (int) Math.ceil(((double) (TOTAL_COMMENT_COUNT)) / COMMENT_PAGE_SIZE);
    public static final int BOOK_ID = 1;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AppConfig appConfig;

    @Mock
    private CommentRepository commentRepository;

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


    @DisplayName("Метод BookPageAttributes должен загружать книгу по id и ее комментарии")
    @Test
    void shouldFindBookById() {
        //Create service
        BookPageService service = new BookPageServiceImpl(appConfig, bookRepository, commentRepository);

        //Set up
        Book book = bookList.get(0);
        PageRequest pageRequest = PageRequest.of(0, COMMENT_PAGE_SIZE);
        PageImpl<Comment> commentPage = new PageImpl<>(COMMENT_PAGE_CONTENT, pageRequest, TOTAL_COMMENT_COUNT);
        //Expected
        BookPageParams expectedParams = new BookPageParams( 0, COMMENT_PAGE_COUNT);
        BookPageAttributes expected = new BookPageAttributes(book, commentPage.toList(), expectedParams);

        //Mocking
        Mockito.when(appConfig.getCommentPageCount()).thenReturn(COMMENT_PAGE_SIZE);
        Mockito.when(bookRepository.findByIdEager(BOOK_ID)).thenReturn(Optional.of(book));
        Mockito.when(commentRepository.findByBook(book, pageRequest)).thenReturn(commentPage);

        //Run service
        BookPageAttributes actual = service.getBookPageAttributes(BOOK_ID, 0, null);

        //Assertions
        Mockito.verify(bookRepository, Mockito.times(1)).findByIdEager(BOOK_ID);
        Mockito.verify(commentRepository, Mockito.times(1)).findByBook(book, pageRequest);
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("Метод BookPageAttributes должен загружать книгу по id и ее комментарии номер страницы должен не выходить за максимум")
    @Test
    void shouldFindBookByIdAndPageNumberShouldNotBeOverThanMaximum() {
        //Create service
        BookPageService service = new BookPageServiceImpl(appConfig, bookRepository, commentRepository);

        //Set up
        Book book = bookList.get(0);
        PageRequest pageRequest = PageRequest.of(COMMENT_PAGE_COUNT - 1, COMMENT_PAGE_SIZE);
        PageImpl<Comment> commentPage = new PageImpl<>(COMMENT_PAGE_CONTENT, pageRequest, TOTAL_COMMENT_COUNT);
        BookPageParams previousParams = new BookPageParams(ANY_CURRENT_PAGE, COMMENT_PAGE_COUNT);

        //Expected
        BookPageParams expectedParams = new BookPageParams(COMMENT_PAGE_COUNT - 1, COMMENT_PAGE_COUNT);
        BookPageAttributes expected = new BookPageAttributes(book, commentPage.toList(), expectedParams);

        //Mocking
        Mockito.when(appConfig.getCommentPageCount()).thenReturn(COMMENT_PAGE_SIZE);
        Mockito.when(bookRepository.findByIdEager(BOOK_ID)).thenReturn(Optional.of(book));
        Mockito.when(commentRepository.findByBook(book, pageRequest)).thenReturn(commentPage);

        //Run service
        BookPageAttributes actual = service.getBookPageAttributes(BOOK_ID, COMMENT_PAGE_COUNT, previousParams);

        //Assertions
        Mockito.verify(bookRepository, Mockito.times(1)).findByIdEager(BOOK_ID);
        Mockito.verify(commentRepository, Mockito.times(1)).findByBook(book, pageRequest);
        Assertions.assertThat(actual).isEqualTo(expected);

    }

}