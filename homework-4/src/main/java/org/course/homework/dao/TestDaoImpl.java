package org.course.homework.dao;

import org.course.homework.dao.interfaces.TestDao;
import org.course.homework.domain.UserTest;
import org.course.homework.domain.User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class TestDaoImpl implements TestDao {

    private Map<Integer, UserTest> tests = new HashMap<>();
    private int currentId;

    @Override
    public List<UserTest> findByUser(User user) {
        return tests.values().stream().filter(test -> test.getUserId() == user.getId()).collect(Collectors.toList());
    }

    @Override
    public UserTest save(UserTest test) {
        currentId++;
        test.setId(currentId);
        return tests.put(currentId, test);
    }
}
