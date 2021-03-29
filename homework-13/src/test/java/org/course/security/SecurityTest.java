package org.course.security;

import org.course.domain.DbPermissionEnum;
import org.course.domain.DbRoleEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

@DisplayName("Security test")
@SpringBootTest
@AutoConfigureMockMvc
class SecurityTest {

    public static final String TEXT = "T";
    public static final long BOOK_ID = 1L;
    private static final int VALUE = 3;
    private static final int PAGE = 1;

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

        mockMvc.perform(MockMvcRequestBuilders.post("/manage/book/delete/1"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());

    }

    @Test
    void shouldSecurePostBookUpdateUrl() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/manage/book/update/1"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());

    }

    @Test
    void shouldSecurePostBookCreateUrl() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/manage/book/create"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());

    }

    @Test
    void shouldSecurePostCommentsUrl() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/comments"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());

    }


    @Test
    void shouldPreventAdminFromPostComments() throws Exception {

        GrantedAuthority authority = new SimpleGrantedAuthority(DbRoleEnum.ADMIN.name());

        mockMvc.perform(MockMvcRequestBuilders.post("/comments")
                .param("bookId", String.valueOf(BOOK_ID))
                .param("text", TEXT)
                .with(SecurityMockMvcRequestPostProcessors.user("admin").authorities(authority))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/access-denied"));

    }

    @Test
    void shouldPreventAdminTraineeFromPostComments() throws Exception {

        GrantedAuthority authority = new SimpleGrantedAuthority(DbRoleEnum.ADMIN_TRAINEE.name());

        mockMvc.perform(MockMvcRequestBuilders.post("/comments")
                .param("bookId", String.valueOf(BOOK_ID))
                .param("text", TEXT)
                .with(SecurityMockMvcRequestPostProcessors.user("admin").authorities(authority))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/access-denied"));

    }

    @Test
    void shouldAllowUserToPostComments() throws Exception {

        GrantedAuthority authority = new SimpleGrantedAuthority(DbRoleEnum.USER.name());

        mockMvc.perform(MockMvcRequestBuilders.post("/comments")
                .param("bookId", String.valueOf(BOOK_ID))
                .param("text", TEXT)
                .with(SecurityMockMvcRequestPostProcessors.user("admin").authorities(authority))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/access-denied"));

    }

    @Test
    void shouldPreventAdminFromPostScores() throws Exception {

        GrantedAuthority authority = new SimpleGrantedAuthority(DbRoleEnum.ADMIN.name());

        mockMvc.perform(MockMvcRequestBuilders.post("/scores")
                .param("bookId", String.valueOf(BOOK_ID))
                .param("value", String.valueOf(VALUE))
                .with(SecurityMockMvcRequestPostProcessors.user("admin").authorities(authority))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/access-denied"));

    }

    @Test
    void shouldPreventAdminTraineeFromPostScores() throws Exception {

        GrantedAuthority authority = new SimpleGrantedAuthority(DbRoleEnum.ADMIN_TRAINEE.name());

        mockMvc.perform(MockMvcRequestBuilders.post("/scores")
                .param("bookId", String.valueOf(BOOK_ID))
                .param("value", String.valueOf(VALUE))
                .with(SecurityMockMvcRequestPostProcessors.user("admin").authorities(authority))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/access-denied"));

    }

    @Test
    void shouldAllowUserToPostScores() throws Exception {

        GrantedAuthority authority = new SimpleGrantedAuthority(DbRoleEnum.USER.name());

        mockMvc.perform(MockMvcRequestBuilders.post("/scores")
                .param("bookId", String.valueOf(BOOK_ID))
                .param("value", String.valueOf(VALUE))
                .with(SecurityMockMvcRequestPostProcessors.user("admin").authorities(authority))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/access-denied"));

    }

    @Test
    void shouldPreventUserFromAccessingManagePage() throws Exception {

        GrantedAuthority authority = new SimpleGrantedAuthority(DbRoleEnum.USER.name());

        mockMvc.perform(MockMvcRequestBuilders.get("/manage")
                .queryParam("page", String.valueOf(PAGE))
                .with(SecurityMockMvcRequestPostProcessors.user("user").authorities(authority)))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/access-denied"));

    }

    @Test
    void shouldPreventUserFromAccessingManageBookCreatePage() throws Exception {

        GrantedAuthority authority = new SimpleGrantedAuthority(DbRoleEnum.USER.name());

        mockMvc.perform(MockMvcRequestBuilders.get("/manage/book/create")
                .queryParam("page", String.valueOf(PAGE))
                .with(SecurityMockMvcRequestPostProcessors.user("user").authorities(authority)))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/access-denied"));

    }

    @Test
    void shouldPreventUserFromAccessingManageBookUpdatePage() throws Exception {

        GrantedAuthority authority = new SimpleGrantedAuthority(DbRoleEnum.USER.name());

        mockMvc.perform(MockMvcRequestBuilders.get("/manage/book/update/1")
                .queryParam("page", String.valueOf(PAGE))
                .with(SecurityMockMvcRequestPostProcessors.user("user").authorities(authority)))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/access-denied"));

    }

    @Test
    void shouldPreventAdminTraineeFromAccessingManageBookDeletePage() throws Exception {

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(DbPermissionEnum.BOOK_CREATE.name()),
                new SimpleGrantedAuthority(DbPermissionEnum.BOOK_UPDATE.name()),
                new SimpleGrantedAuthority(DbPermissionEnum.BOOK_MANAGE.name()));

        mockMvc.perform(MockMvcRequestBuilders.post("/manage/book/delete/1")
                .queryParam("page", String.valueOf(PAGE))
                .with(SecurityMockMvcRequestPostProcessors.user("user").authorities(authorities)))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/access-denied"));

    }

}