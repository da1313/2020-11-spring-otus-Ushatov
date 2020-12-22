package org.course.homework.service;

import lombok.AllArgsConstructor;
import org.course.homework.dao.interfaces.TestDao;
import org.course.homework.domain.UserTest;
import org.course.homework.domain.User;
import org.course.homework.service.interfaces.StatisticService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class StatisticServiceImpl implements StatisticService {

    private final TestDao testDao;

    @Override
    public String getStatistics(User user) throws IllegalArgumentException {
        List<UserTest> userTests = testDao.findByUser(user);
        if (userTests.isEmpty()) {
            throw new IllegalArgumentException("Statistics can not be calculated with empty test data!");
        }
        StringBuilder buffer = new StringBuilder();
        int attempts = userTests.size();
        for (int i = 0; i < attempts; i++) {
            double nextResult = userTests.get(i).getRate();
            buffer.append(nextResult);
            if (i < attempts - 1) buffer.append("\n");
        }
        return buffer.toString();
    }
}
