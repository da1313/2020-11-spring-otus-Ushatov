package org.course.homework.service;

import org.course.homework.dao.interfaces.UserDao;
import org.course.homework.domain.User;
import org.course.homework.service.interfaces.LoginService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Class LoginServiceImpl")
@ExtendWith(MockitoExtension.class)
class LoginServiceImplTest {

    @Mock
    private LoginContext loginContext;
    @Mock
    private UserDao userDao;
    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    public static String USER_NAME = "Ivan";

    @DisplayName("Новый пользователь записан в контекст")
    @Test
    void loginNewUser() {
        Mockito.when(userDao.findByName(USER_NAME)).thenReturn(Optional.empty());
        LoginService loginService = new LoginServiceImpl(loginContext, userDao);
        loginService.login(USER_NAME);
        Mockito.verify(loginContext).setUser(userArgumentCaptor.capture());
        assertEquals(USER_NAME, userArgumentCaptor.getValue().getName());
    }

    @DisplayName("Существующий пользователь записан в контекст")
    @Test
    void loginExistingUser() {
        User user = new User();
        user.setId(0);
        user.setName(USER_NAME);
        Mockito.when(userDao.findByName(USER_NAME)).thenReturn(Optional.of(user));
        LoginService loginService = new LoginServiceImpl(loginContext, userDao);
        loginService.login(USER_NAME);
        Mockito.verify(loginContext).setUser(userArgumentCaptor.capture());
        assertEquals(USER_NAME, userArgumentCaptor.getValue().getName());
    }

    @DisplayName("Пользователь не залогинен")
    @Test
    void checkUserFalse() {
        Mockito.when(loginContext.getUser()).thenReturn(null);
        LoginService loginService = new LoginServiceImpl(loginContext, userDao);
        boolean isLoggedIn = loginService.checkUser();
        assertFalse(isLoggedIn);
    }

    @DisplayName("Пользователь залогинен")
    @Test
    void checkUserTrue() {
        User user = new User();
        user.setId(0);
        user.setName(USER_NAME);
        Mockito.when(loginContext.getUser()).thenReturn(user);
        LoginService loginService = new LoginServiceImpl(loginContext, userDao);
        boolean isLoggedIn = loginService.checkUser();
        assertTrue(isLoggedIn);
    }

    @DisplayName("Из контекста возвращен правильный пользователь")
    @Test
    void getUser() {
        User user = new User();
        user.setId(0);
        user.setName(USER_NAME);
        Mockito.when(loginContext.getUser()).thenReturn(user);
        LoginService loginService = new LoginServiceImpl(loginContext, userDao);
        User loggedUser = loginService.getUser();
        assertEquals(user, loggedUser);
    }
}