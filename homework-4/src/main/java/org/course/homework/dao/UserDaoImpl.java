package org.course.homework.dao;

import org.course.homework.dao.interfaces.UserDao;
import org.course.homework.domain.User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {

    private Map<Integer, User> users = new HashMap<>();
    private int currentId;

    @Override
    public User save(User user) {
        currentId++;
        user.setId(currentId);
        return users.put(currentId, user);
    }

    @Override
    public Optional<User> findByName(String name) {
        return users.values().stream().filter(e -> e.getName().equals(name)).findFirst();
    }
}
