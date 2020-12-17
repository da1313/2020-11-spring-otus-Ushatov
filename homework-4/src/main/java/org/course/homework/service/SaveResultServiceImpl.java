package org.course.homework.service;

import lombok.AllArgsConstructor;
import org.course.homework.dao.interfaces.TestDao;
import org.course.homework.domain.UserTest;
import org.course.homework.domain.User;
import org.course.homework.service.interfaces.SaveResultService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SaveResultServiceImpl implements SaveResultService {

    private final TestDao testDao;

    @Override
    public void saveResult(double rate, User user) {
        UserTest test = new UserTest();
        test.setRate(rate);
        test.setUserId(user.getId());
        testDao.save(test);
    }

}
