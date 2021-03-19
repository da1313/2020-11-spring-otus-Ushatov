package org.course.controllers;

import org.course.domain.*;
import org.course.dto.request.BookRequest;
import org.course.service.interfaces.BookHandleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.stream.Collectors;

@DisplayName("Class BookController")
@WebMvcTest(BookController.class)
class BookControllerTest {

    public static final int BOOK_ID = 1;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookHandleService bookHandleService;

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
    void shouldInvokeCreateBookMethodAndRedirectToMainPage() throws Exception {

        Book book = bookList.get(0);

        List<Long> genreIds = book.getGenres().stream().map(Genre::getId).collect(Collectors.toList());

        BookRequest bookRequest = new BookRequest(book.getTitle(), genreIds, book.getAuthor().getId(), book.getDescription());

        mvc.perform(MockMvcRequestBuilders.post("/book/create")
                .param("title", book.getTitle())
                .param("genreIds", String.valueOf(genreIds.get(0)))
                .param("genreIds", String.valueOf(genreIds.get(1)))
                .param("authorId", String.valueOf(book.getAuthor().getId()))
                .param("description", book.getDescription()))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/"));

        Mockito.verify(bookHandleService, Mockito.times(1))
                .createBook(bookRequest);

    }

    @Test
    void shouldInvokeUpdateBookMethodAndRedirectToMainPage() throws Exception {

        Book book = bookList.get(0);

        List<Long> genreIds = List.of(1L, 2L);

        BookRequest bookRequest = new BookRequest(book.getTitle(), genreIds, book.getAuthor().getId(), book.getDescription());

        mvc.perform(MockMvcRequestBuilders.post("/book/update/" + book.getId())
                .param("title", book.getTitle())
                .param("genreIds", String.valueOf(genreIds.get(0)))
                .param("genreIds", String.valueOf(genreIds.get(1)))
                .param("authorId", String.valueOf(book.getAuthor().getId()))
                .param("description", book.getDescription()))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/"));

        Mockito.verify(bookHandleService, Mockito.times(1))
                .updateBook(book.getId(), bookRequest);

    }


    @Test
    void shouldInvokeDeleteMethodAndRedirectToMainPage() throws Exception {

        mvc.perform(MockMvcRequestBuilders.post("/book/delete/" + BOOK_ID))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/"));

        Mockito.verify(bookHandleService, Mockito.times(1)).deleteBook(BOOK_ID);

    }
}