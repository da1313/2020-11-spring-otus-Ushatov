package org.course.homework.service.interfaces;

import org.course.homework.domain.User;

public interface LoginService {
    void login(String username);

    boolean checkUser();

    User getUser();
}
