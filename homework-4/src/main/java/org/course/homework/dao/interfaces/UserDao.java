package org.course.homework.dao.interfaces;

import org.course.homework.domain.User;

import java.util.Optional;

public interface UserDao {
    User save(User user);
    Optional<User> findByName(String name);
}
