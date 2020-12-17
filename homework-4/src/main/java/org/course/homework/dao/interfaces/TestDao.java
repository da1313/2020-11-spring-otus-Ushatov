package org.course.homework.dao.interfaces;

import org.course.homework.domain.UserTest;
import org.course.homework.domain.User;

import java.util.List;

public interface TestDao {
    List<UserTest> findByUser(User user);
    UserTest save(UserTest test);
}
