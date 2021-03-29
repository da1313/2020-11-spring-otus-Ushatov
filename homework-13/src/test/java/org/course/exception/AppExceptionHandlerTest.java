package org.course.exception;

import org.course.config.AppConfig;
import org.course.controllers.PageController;
import org.course.repository.AuthorRepository;
import org.course.repository.BookRepository;
import org.course.repository.CommentRepository;
import org.course.repository.GenreRepository;
import org.course.security.DaoUserDetailsService;
import org.course.service.PagesServiceImpl;
import org.course.service.interfaces.PagingService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

@DisplayName("Class AppExceptionHandler")
@WebMvcTest(PageController.class)
@Import(PagesServiceImpl.class)
class AppExceptionHandlerTest {

    public static final long BOOK_ID = 1L;
    public static final String PAGE = "1";
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private GenreRepository genreRepository;

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private PagingService pagingService;

    @MockBean
    private AppConfig appConfig;

    @MockBean(name = "daoUserDetailsService")
    private DaoUserDetailsService daoUserDetailsService;

    @MockBean(name = "customAccessDeniedHandler")
    private AccessDeniedHandler accessDeniedHandler;

    @Test
    void shouldRedirectOnErrorPageOnEntityException() throws Exception {

        Mockito.when(bookRepository.findByIdEager(BOOK_ID)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/books/" + BOOK_ID)
                .with(SecurityMockMvcRequestPostProcessors.user("user"))
                .queryParam("page", PAGE))
                .andExpect(MockMvcResultMatchers.view().name("error"));

    }
}