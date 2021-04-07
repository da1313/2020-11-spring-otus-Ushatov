package org.course.controllers;

import org.assertj.core.api.Assertions;
import org.course.api.request.CommentRequest;
import org.course.api.request.ScoreRequest;
import org.course.domain.DbRoleEnum;
import org.course.security.DaoUserDetailsService;
import org.course.service.interfaces.CommentService;
import org.course.service.interfaces.ScoreService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Class ScoreController")
@WebMvcTest(ScoreController.class)
class ScoreControllerTest {

    private static final long BOOK_ID = 1L;
    public static final int VALUE = 1;
    @MockBean
    private ScoreService scoreService;

    @MockBean(name = "daoUserDetailsService")
    private DaoUserDetailsService daoUserDetailsService;

    @MockBean(name = "customAccessDeniedHandler")
    private AccessDeniedHandler accessDeniedHandler;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldCreateScoreAndReturnToBookPage() throws Exception {

        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + DbRoleEnum.USER.name());

        ScoreRequest request = new ScoreRequest(BOOK_ID, VALUE);

        mockMvc.perform(MockMvcRequestBuilders.post("/scores")
                .param("bookId", String.valueOf(BOOK_ID))
                .param("value", String.valueOf(VALUE))
                .with(SecurityMockMvcRequestPostProcessors.user("user").authorities(authority))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/books/" + BOOK_ID));

        ArgumentCaptor<ScoreRequest> scoreRequestArgumentCaptor = ArgumentCaptor.forClass(ScoreRequest.class);

        Mockito.verify(scoreService).createScore(scoreRequestArgumentCaptor.capture());

        ScoreRequest actual = scoreRequestArgumentCaptor.getValue();

        Assertions.assertThat(actual).isEqualTo(request);

    }
}