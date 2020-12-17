package org.course.homework.dao;

import org.course.homework.dao.interfaces.TestDao;
import org.course.homework.domain.User;
import org.course.homework.domain.UserTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Class TestDaoImpl")
class TestDaoImplTest {

    public static final int USER_ID = 0;
    public final static String USER_NAME = "Ivan";

    @DisplayName("Сохранение новой записи и получение списка работают")
    @Test
    void shouldSaveTestsAndThenGetSame() {
        TestDao testDao = new TestDaoImpl();
        User user = new User();
        user.setId(USER_ID);
        user.setName(USER_NAME);
        List<UserTest> userTestList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            UserTest userTest = new UserTest();
            userTest.setId(i);
            userTest.setRate(i * 10);
            userTest.setUserId(USER_ID);
            userTestList.add(userTest);
            testDao.save(userTest);
        }
        assertIterableEquals(userTestList, testDao.findByUser(user));
    }
}