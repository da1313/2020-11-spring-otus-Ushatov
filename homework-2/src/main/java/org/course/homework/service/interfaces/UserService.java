package org.course.homework.service.interfaces;

import org.course.homework.domain.Answer;

import java.util.List;

public interface UserService {
    String readUserName();
    List<Answer> readAnswer(List<Answer> answers);
}
