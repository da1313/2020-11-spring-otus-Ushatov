package org.course.security;

import org.course.service.interfaces.BookService;
import org.course.service.interfaces.CommentService;
import org.course.service.interfaces.PagesService;
import org.course.service.interfaces.ScoreService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@DisplayName("Security test")
@WebMvcTest
class SecurityTest {

    @MockBean
    private BookService bookService;

    @MockBean
    private CommentService commentService;

    @MockBean
    private PagesService pagesService;

    @MockBean
    private ScoreService scoreService;

    @MockBean(name = "daoUserDetailsService")
    private DaoUserDetailsService daoUserDetailsService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldSecureGetBooksUrl() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/books"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());

    }

    @Test
    void shouldSecureGetBookUrl() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/books/1"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());

    }

    @Test
    void shouldSecureGetManageUrl() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/manage"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());

    }

    @Test
    void shouldSecureGetBookUpdateUrl() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/book/update/1"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());

    }

    @Test
    void shouldSecureGetBookCreateUrl() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/book/create"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());

    }

    @Test
    void shouldSecurePostScoresUrl() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/scores"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());

    }

    @Test
    void shouldSecurePostBookDeleteUrl() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/book/delete/1"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());

    }

    @Test
    void shouldSecurePostBookUpdateUrl() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/book/update/1"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());

    }

    @Test
    void shouldSecurePostBookCreateUrl() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/book/create"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());

    }

    @Test
    void shouldSecurePostCommentsUrl() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/comments"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());

    }


}