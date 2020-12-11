package org.course.homework.service.interfaces;

import org.course.homework.domain.Answer;

import java.util.List;

public interface TestAdvisorService {

    boolean validate(List<Answer> answers, List<Answer> userAnswers);

    void printResult(String userName, int userAnswers, int questionCount);

}
