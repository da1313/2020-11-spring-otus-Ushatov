package org.course.controllers;

import org.course.service.interfaces.CommentHandleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@DisplayName("Class CommentController")
@WebMvcTest(CommentController.class)
class CommentControllerTest {

    public static final long BOOK_ID = 1L;
    public static final String COMMENT_TEXT = "some text";
    @Autowired
    private MockMvc mvc;

    @MockBean
    private CommentHandleService commentHandleService;

    @Test
    void shouldInvokeCreateCommentMethodAndRedirectToMainPage() throws Exception {

        mvc.perform(MockMvcRequestBuilders.post("/comment/" + BOOK_ID)
                .param("text", COMMENT_TEXT))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/" + BOOK_ID));

        Mockito.verify(commentHandleService, Mockito.times(1)).createComment(BOOK_ID, COMMENT_TEXT);

    }
}