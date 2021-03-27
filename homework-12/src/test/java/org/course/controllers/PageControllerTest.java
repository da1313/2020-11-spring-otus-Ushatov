package org.course.controllers;

import org.course.api.attributes.BookPageAttributes;
import org.course.api.attributes.BooksPageAttributes;
import org.course.api.attributes.PagingAttributes;
import org.course.api.attributes.UpdateBookPageAttributes;
import org.course.domain.*;
import org.course.security.DaoUserDetailsService;
import org.course.service.interfaces.PagesService;
import org.course.service.interfaces.ScoreService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.web.util.RedirectUrlBuilder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Class PageController")
@WebMvcTest(PageController.class)
class PageControllerTest {

    public static final long BOOK_ID = 1L;
    public static final User USER = new User();
    public static final Author AUTHOR = new Author(1L, "A");
    public static final List<Author> AUTHORS = List.of(AUTHOR);
    public static final Set<Genre> GENRES = Set.of(new Genre(1L, "G"));
    public static final ArrayList<Genre> GENRES_LIST = new ArrayList<>(GENRES);
    public static final Book BOOK = new Book(BOOK_ID, "T", "D", LocalDateTime.now(), AUTHOR, GENRES, new Info());
    public static final List<Book> BOOK_LIST = List.of(BOOK);
    public static final List<Comment> COMMENT_LIST = List.of(new Comment(1L, "T", LocalDateTime.now(), BOOK, USER));
    public static final PagingAttributes PAGING_ATTRIBUTES = new PagingAttributes(3, List.of(1), List.of(3), 2, 3, 1);

    public static final int PAGE = 1;
    @MockBean
    private PagesService pagesService;

    @MockBean(name = "daoUserDetailsService")
    private DaoUserDetailsService daoUserDetailsService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldMapCustomLoginView() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/login"))
                .andExpect(MockMvcResultMatchers.view().name("login"));

    }

    @Test
    void shouldRedirectOnLoginWhenLogout() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/logout"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("http://localhost/login"));

    }

    @Test
    void shouldRedirectToBooksPages() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/")
                .with(SecurityMockMvcRequestPostProcessors.user("user")))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/books"));

    }

    @Test
    void shouldMapBookView() throws Exception {

        BookPageAttributes attributes = new BookPageAttributes(BOOK, COMMENT_LIST, PAGING_ATTRIBUTES);

        Mockito.when(pagesService.getBookPageAttributes(BOOK_ID, PAGE)).thenReturn(attributes);

        mockMvc.perform(MockMvcRequestBuilders.get("/books/" + BOOK_ID)
                .queryParam("page", String.valueOf(PAGE))
                .with(SecurityMockMvcRequestPostProcessors.user(USER)))
                .andExpect(MockMvcResultMatchers.model().attribute("book", BOOK))
                .andExpect(MockMvcResultMatchers.model().attribute("comments", COMMENT_LIST))
                .andExpect(MockMvcResultMatchers.model().attribute("page", PAGING_ATTRIBUTES))
                .andExpect(MockMvcResultMatchers.model().attribute("email", USER.getEmail()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("book"));


    }

    @Test
    void shouldMapBooksView() throws Exception {

        BooksPageAttributes attributes = new BooksPageAttributes(BOOK_LIST, PAGING_ATTRIBUTES);

        Mockito.when(pagesService.getBooksPageAttributes(PAGE)).thenReturn(attributes);

        mockMvc.perform(MockMvcRequestBuilders.get("/books")
                .queryParam("page", String.valueOf(PAGE))
                .with(SecurityMockMvcRequestPostProcessors.user(USER)))
                .andExpect(MockMvcResultMatchers.model().attribute("bookList", BOOK_LIST))
                .andExpect(MockMvcResultMatchers.model().attribute("page", PAGING_ATTRIBUTES))
                .andExpect(MockMvcResultMatchers.model().attribute("email", USER.getEmail()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("books"));

    }

    @Test
    void shouldMapManageView() throws Exception {

        BooksPageAttributes attributes = new BooksPageAttributes(BOOK_LIST, PAGING_ATTRIBUTES);

        Mockito.when(pagesService.getBooksPageAttributes(PAGE)).thenReturn(attributes);

        mockMvc.perform(MockMvcRequestBuilders.get("/manage")
                .queryParam("page", String.valueOf(PAGE))
                .with(SecurityMockMvcRequestPostProcessors.user(USER)))
                .andExpect(MockMvcResultMatchers.model().attribute("bookList", BOOK_LIST))
                .andExpect(MockMvcResultMatchers.model().attribute("page", PAGING_ATTRIBUTES))
                .andExpect(MockMvcResultMatchers.model().attribute("managePage", PAGE))
                .andExpect(MockMvcResultMatchers.model().attribute("email", USER.getEmail()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("manage"));

    }

    @Test
    void shouldMapUpdateBookView() throws Exception {

        UpdateBookPageAttributes attributes = new UpdateBookPageAttributes(AUTHORS, GENRES_LIST, BOOK);

        Mockito.when(pagesService.getUpdateBookPageAttributes(BOOK_ID)).thenReturn(attributes);

        mockMvc.perform(MockMvcRequestBuilders.get("/book/update/" + BOOK_ID)
                .with(SecurityMockMvcRequestPostProcessors.user(USER)))
                .andExpect(MockMvcResultMatchers.model().attribute("authors", AUTHORS))
                .andExpect(MockMvcResultMatchers.model().attribute("genres", GENRES_LIST))
                .andExpect(MockMvcResultMatchers.model().attribute("book", BOOK))
                .andExpect(MockMvcResultMatchers.model().attribute("email", USER.getEmail()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("update"));

    }

    @Test
    void shouldMapCreateBookView() throws Exception {

        UpdateBookPageAttributes attributes = new UpdateBookPageAttributes(AUTHORS, GENRES_LIST, null);

        Mockito.when(pagesService.getCreateBookPageAttributes()).thenReturn(attributes);

        mockMvc.perform(MockMvcRequestBuilders.get("/book/create")
                .with(SecurityMockMvcRequestPostProcessors.user(USER)))
                .andExpect(MockMvcResultMatchers.model().attribute("authors", AUTHORS))
                .andExpect(MockMvcResultMatchers.model().attribute("genres", GENRES_LIST))
                .andExpect(MockMvcResultMatchers.model().attribute("email", USER.getEmail()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("create"));

    }
}