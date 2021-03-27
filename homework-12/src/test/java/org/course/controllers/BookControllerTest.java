package org.course.controllers;

import liquibase.pro.packaged.T;
import org.assertj.core.api.Assertions;
import org.course.api.request.BookRequest;
import org.course.security.DaoUserDetailsService;
import org.course.service.interfaces.BookService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@DisplayName("Class BookController")
@WebMvcTest(BookController.class)
class BookControllerTest {

    public static final long BOOK_ID = 1L;
    public static final String TITLE = "T1";
    public static final List<Long> GENRE_IDS = List.of(1L, 2L);
    public static final int AUTHOR_ID = 1;
    public static final String DESCRIPTION = "D";
    public static final int PAGE = 1;

    @MockBean
    private BookService bookService;

    @MockBean(name = "daoUserDetailsService")
    private DaoUserDetailsService daoUserDetailsService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldInvokeDeleteBookMethodAndRedirectToManagePageWithSameNumber() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/book/delete/" + BOOK_ID)
                .with(SecurityMockMvcRequestPostProcessors.user("user"))
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .sessionAttr("managePage", PAGE))
                .andExpect(MockMvcResultMatchers.redirectedUrlTemplate(UriComponentsBuilder.fromPath("/manage")
                        .queryParam("page",PAGE).toUriString()));

        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);

        Mockito.verify(bookService).deleteBook(argumentCaptor.capture());

        Long actual = argumentCaptor.getValue();

        Assertions.assertThat(actual).isEqualTo(BOOK_ID);

    }

    @Test
    void shouldInvokeUpdateBookMethodAndRedirectToManagePageWithSameNumber() throws Exception {

        BookRequest bookRequest = new BookRequest(TITLE, GENRE_IDS, AUTHOR_ID, DESCRIPTION);

        mockMvc.perform(MockMvcRequestBuilders.post("/book/update/" + BOOK_ID)
                .param("title", TITLE)
                .param("genreIds", String.valueOf(GENRE_IDS.get(0)), String.valueOf(GENRE_IDS.get(1)))
                .param("authorId", String.valueOf(AUTHOR_ID))
                .param("description", DESCRIPTION)
                .with(SecurityMockMvcRequestPostProcessors.user("user"))
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .sessionAttr("managePage", PAGE))
                .andExpect(MockMvcResultMatchers.redirectedUrlTemplate(UriComponentsBuilder.fromPath("/manage")
                        .queryParam("page",PAGE).toUriString()));

        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);

        ArgumentCaptor<BookRequest> requestArgumentCaptor = ArgumentCaptor.forClass(BookRequest.class);

        Mockito.verify(bookService).updateBook(argumentCaptor.capture(), requestArgumentCaptor.capture());

        Long actualBookId = argumentCaptor.getValue();

        BookRequest actualRequest = requestArgumentCaptor.getValue();

        Assertions.assertThat(actualBookId).isEqualTo(BOOK_ID);

        Assertions.assertThat(actualRequest).isEqualTo(bookRequest);

    }

    @Test
    void shouldInvokeCreateBookMethodAndRedirectToManagePageWithSameNumber() throws Exception {

        BookRequest bookRequest = new BookRequest(TITLE, GENRE_IDS, AUTHOR_ID, DESCRIPTION);

        mockMvc.perform(MockMvcRequestBuilders.post("/book/create")
                .param("title", TITLE)
                .param("genreIds", String.valueOf(GENRE_IDS.get(0)), String.valueOf(GENRE_IDS.get(1)))
                .param("authorId", String.valueOf(AUTHOR_ID))
                .param("description", DESCRIPTION)
                .with(SecurityMockMvcRequestPostProcessors.user("user"))
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .sessionAttr("managePage", PAGE))
                .andExpect(MockMvcResultMatchers.redirectedUrlTemplate(UriComponentsBuilder.fromPath("/manage")
                        .queryParam("page",PAGE).toUriString()));

        ArgumentCaptor<BookRequest> requestArgumentCaptor = ArgumentCaptor.forClass(BookRequest.class);

        Mockito.verify(bookService).createBook(requestArgumentCaptor.capture());

        BookRequest actualRequest = requestArgumentCaptor.getValue();

        Assertions.assertThat(actualRequest).isEqualTo(bookRequest);

    }
}