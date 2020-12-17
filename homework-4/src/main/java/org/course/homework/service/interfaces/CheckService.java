package org.course.homework.service.interfaces;

import org.course.homework.domain.Answer;

import java.util.List;

public interface CheckService {
    boolean checkAnswer(List<Answer> userAnswers, List<Answer> correctAnswers);
    double getUserRate(int userValidAnswers, int questionCount);
}
