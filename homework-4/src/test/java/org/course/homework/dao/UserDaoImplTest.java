package org.course.homework.dao;

import org.course.homework.dao.interfaces.UserDao;
import org.course.homework.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Class UserDaoImpl")
class UserDaoImplTest {

    public static final int COUNT = 5;

    @DisplayName("Сохранение новой записи и получение списка работают")
    @Test
    void shouldSaveUsersAndThenGetSame() {
        UserDao userDao = new UserDaoImpl();
        List<User> userList = new ArrayList<>();
        for (int i = 0; i < COUNT; i++) {
            User user = new User();
            user.setId(i);
            user.setName("USER_NAME " + i);
            userList.add(user);
            userDao.save(user);
        }
        for (int i = 0; i < COUNT; i++) {
            assertEquals(userList.get(i), userDao.findByName("USER_NAME " + i).get());
        }
    }
}