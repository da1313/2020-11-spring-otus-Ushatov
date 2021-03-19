package org.course.controllers;

import org.course.domain.*;
import org.course.dto.attributes.BookPageAttributes;
import org.course.dto.attributes.CreateBookPageAttributes;
import org.course.dto.attributes.MainPageAttributes;
import org.course.dto.attributes.UpdateBookPageAttributes;
import org.course.dto.request.MainPageRequest;
import org.course.dto.state.BookPageParams;
import org.course.dto.state.MainPageParams;
import org.course.service.interfaces.*;
import org.course.service.interfaces.handlers.MainPageHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@DisplayName("Class PageController")
@WebMvcTest(PageController.class)
class PageControllerTest {

    public static final int GENRE_ID = 0;
    public static final boolean IS_SEARCH = false;
    public static final int BOOK_ID = 1;
    public static final int CURRENT_PAGE = 0;
    public static final int TOTAL_PAGES = 5;
    public static final int NEXT_PAGE = 0;
    public static final String QUERY = "";
    public static final MainPageParams PREVIOUS_MAIN_PARAMS = null;
    public static final BookPageParams PREVIOUS_BOOK_PARAMS = null;

    @Autowired
    private MockMvc mvc;

    @MockBean
    @Qualifier("mainPageHandlerController")
    private MainPageHandler mainPageHandler;

    @MockBean
    private BookPageService bookPageService;

    @MockBean
    private CreateBookPageService createBookPageService;

    @MockBean
    private UpdateBookPageService updateBookPageService;

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
    void shouldReturnMainViewAndSetAttributes() throws Exception {

        MainPageParams mainPageParams = new MainPageParams();

        MainPageAttributes mainPageAttributes = new MainPageAttributes(mainPageParams, bookList, genreList);

        MainPageRequest request = new MainPageRequest();
        MainPageParams pageParams = null;

        Mockito.when(mainPageHandler.getMainPageAttributes(request, pageParams))
                .thenReturn(mainPageAttributes);

        mvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("main"))
                .andExpect(MockMvcResultMatchers.model()
                        .attribute("mainPageParams", mainPageParams))
                .andExpect(MockMvcResultMatchers.model().attribute("books", bookList))
                .andExpect(MockMvcResultMatchers.model().attribute("genres", genreList));

    }

    @Test
    void shouldReturnBookViewAndSetAttributes() throws Exception {

        BookPageParams bookPageParams = new BookPageParams(CURRENT_PAGE, TOTAL_PAGES);

        BookPageAttributes bookPageAttributes = new BookPageAttributes(bookList.get(0), bookList.get(0).getComments(), bookPageParams);

        Mockito.when(bookPageService.getBookPageAttributes(BOOK_ID, NEXT_PAGE, PREVIOUS_BOOK_PARAMS)).thenReturn(bookPageAttributes);

        mvc.perform(MockMvcRequestBuilders.get("/" + BOOK_ID))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("book"))
                .andExpect(MockMvcResultMatchers.model().attribute("bookPageParams", bookPageParams))
                .andExpect(MockMvcResultMatchers.model().attribute("book", bookList.get(0)))
                .andExpect(MockMvcResultMatchers.model().attribute("comments", bookList.get(0).getComments()));

    }

    @Test
    void shouldReturnCreateViewAndSetAttributes() throws Exception {

        CreateBookPageAttributes attributes = new CreateBookPageAttributes(genreList, authorList);

        Mockito.when(createBookPageService.getCreateBookPageAttributes()).thenReturn(attributes);

        mvc.perform(MockMvcRequestBuilders.get("/book/create"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("create"))
                .andExpect(MockMvcResultMatchers.model().attribute("authors", authorList))
                .andExpect(MockMvcResultMatchers.model().attribute("genres", genreList));

    }

    @Test
    void shouldReturnUpdateViewAndSetAttributes() throws Exception {

        Book book = bookList.get(0);

        UpdateBookPageAttributes attributes = new UpdateBookPageAttributes(genreList, authorList, book);

        Mockito.when(updateBookPageService.getUpdateBookAttributes(book.getId())).thenReturn(attributes);

        mvc.perform(MockMvcRequestBuilders.get("/book/update/" + book.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("update"))
                .andExpect(MockMvcResultMatchers.model().attribute("book", book))
                .andExpect(MockMvcResultMatchers.model().attribute("authors", authorList))
                .andExpect(MockMvcResultMatchers.model().attribute("genres", genreList));

    }

}