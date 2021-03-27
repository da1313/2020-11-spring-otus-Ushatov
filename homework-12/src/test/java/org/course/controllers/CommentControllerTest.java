package org.course.controllers;

import org.assertj.core.api.Assertions;
import org.course.api.request.CommentRequest;
import org.course.security.DaoUserDetailsService;
import org.course.service.interfaces.BookService;
import org.course.service.interfaces.CommentService;
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

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Class CommentController")
@WebMvcTest(CommentController.class)
class CommentControllerTest {

    public static final String TEXT = "T";
    public static final long BOOK_ID = 1L;
    @MockBean
    private CommentService commentService;

    @MockBean(name = "daoUserDetailsService")
    private DaoUserDetailsService daoUserDetailsService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldCreateCommentAndReturnOnBookPage() throws Exception {

        CommentRequest request = new CommentRequest(BOOK_ID, TEXT);

        mockMvc.perform(MockMvcRequestBuilders.post("/comments")
                .param("bookId", String.valueOf(BOOK_ID))
                .param("text", TEXT)
                .with(SecurityMockMvcRequestPostProcessors.user("user"))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/books/" + BOOK_ID));

        ArgumentCaptor<CommentRequest> commentRequestArgumentCaptor = ArgumentCaptor.forClass(CommentRequest.class);

        Mockito.verify(commentService).createComment(commentRequestArgumentCaptor.capture());

        CommentRequest actual = commentRequestArgumentCaptor.getValue();

        Assertions.assertThat(actual).isEqualTo(request);

    }
}