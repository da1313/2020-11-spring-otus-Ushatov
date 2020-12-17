package org.course.homework.service;

import lombok.AllArgsConstructor;
import org.course.homework.aspect.LogMethod;
import org.course.homework.dao.interfaces.UserDao;
import org.course.homework.domain.User;
import org.course.homework.service.interfaces.LoginService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final LoginContext loginContext;
    private final UserDao userDao;

    @LogMethod(argsPattern = "User %s logged in", argsOrder = 0)
    @Override
    public void login(String username){
        Optional<User> optional = userDao.findByName(username);
        optional.ifPresentOrElse(loginContext::setUser, () ->{
            User newUser = new User();
            newUser.setName(username);
            userDao.save(newUser);
            loginContext.setUser(newUser);
        });
    }

    @Override
    public boolean checkUser(){
        return loginContext.getUser() != null;
    }

    @Override
    public User getUser(){
        return loginContext.getUser();
    }
}
