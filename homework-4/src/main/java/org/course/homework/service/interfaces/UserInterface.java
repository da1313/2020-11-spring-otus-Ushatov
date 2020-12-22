package org.course.homework.service.interfaces;

import org.course.homework.domain.Answer;
import org.course.homework.domain.Question;

import java.util.List;

public interface UserInterface {
    void printQuestion(Question question, int questionNumber);
    void printAnswers(List<Answer> answerList);
    List<Answer> readUserAnswers(Question question);
    void printResult(String userName, double userRate, double successRate);
}
