package org.course.homework.service;

import org.course.homework.dao.interfaces.TestDao;
import org.course.homework.domain.User;
import org.course.homework.domain.UserTest;
import org.course.homework.service.interfaces.SaveResultService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Class SaveResultServiceImpl")
@ExtendWith(MockitoExtension.class)
class SaveResultServiceImplTest {

    @Mock
    private TestDao testDao;

    @Captor
    private ArgumentCaptor<UserTest> userTestArgumentCaptor;

    public final static int USER_ID = 0;
    public final static double USER_RATE = 100;
    public final static String USER_NAME = "Ivan";

    @DisplayName("Тест сохранен с нужным пользователем")
    @Test
    void saveResult() {
        User user = new User();
        user.setId(USER_ID);
        user.setName(USER_NAME);
        SaveResultService saveResultService = new SaveResultServiceImpl(testDao);
        saveResultService.saveResult(USER_RATE, user);
        Mockito.verify(testDao).save(userTestArgumentCaptor.capture());
        assertAll(
                () -> assertEquals(USER_ID, userTestArgumentCaptor.getValue().getUserId()),
                () -> assertEquals(USER_RATE, userTestArgumentCaptor.getValue().getRate())
        );
    }
}