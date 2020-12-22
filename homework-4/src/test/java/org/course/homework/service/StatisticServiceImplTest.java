package org.course.homework.service;

import org.course.homework.dao.interfaces.TestDao;
import org.course.homework.domain.User;
import org.course.homework.domain.UserTest;
import org.course.homework.service.interfaces.StatisticService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Class StatisticServiceImpl")
@ExtendWith(MockitoExtension.class)
class StatisticServiceImplTest {

    @Mock
    private TestDao testDao;

    public final static int USER_ID = 0;
    public final static String USER_NAME = "Ivan";

    @DisplayName("Статистика считается корректно")
    @Test
    void getStatistics() {
        User user = new User();
        user.setId(USER_ID);
        user.setName(USER_NAME);
        List<UserTest> userTestList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            UserTest userTest = new UserTest();
            userTest.setId(i);
            userTest.setRate(i * 10);
            userTest.setUserId(user.getId());
            userTestList.add(userTest);
        }
        Mockito.when(testDao.findByUser(user)).thenReturn(userTestList);
        StatisticService statisticService = new StatisticServiceImpl(testDao);
        String output = statisticService.getStatistics(user);
        assertEquals("0.0\n10.0\n20.0\n30.0\n40.0", output);
    }

    @DisplayName("Кидается исключение если список тестов пуст")
    @Test
    void shouldThrowExceptionOnEmptyTestList(){
        User user = new User();
        user.setId(USER_ID);
        user.setName(USER_NAME);
        List<UserTest> userTestList = new ArrayList<>();
        Mockito.when(testDao.findByUser(user)).thenReturn(userTestList);
        StatisticService statisticService = new StatisticServiceImpl(testDao);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> statisticService.getStatistics(user));
        assertEquals("Statistics can not be calculated with empty test data!", exception.getMessage());
    }
}