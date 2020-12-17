package org.course.homework.service;

import lombok.Getter;
import lombok.Setter;
import org.course.homework.domain.User;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
public class LoginContext {
    private User user;
}
