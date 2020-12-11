package org.course.homework.service;

import org.course.homework.domain.Answer;
import org.course.homework.service.interfaces.PrintService;
import org.course.homework.service.interfaces.TestAdvisorService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestAdvisorServiceImpl implements TestAdvisorService {

    private final PrintService printService;
    private final double passRate;

    public TestAdvisorServiceImpl(PrintService printService, @Value("${passValue}") double passRate) {
        this.printService = printService;
        this.passRate = passRate;
    }

    @Override
    public boolean validate(List<Answer> answers, List<Answer> userAnswers) {
        long correctAnswersCount = answers.stream().filter(Answer::isCorrect).count();
        return userAnswers.size() == correctAnswersCount && userAnswers.stream().allMatch(Answer::isCorrect);
    }

    @Override
    public void printResult(String userName, int userAnswers, int questionCount) {
        double rate = ((double) userAnswers / questionCount) * 100;
        if (rate < passRate) {
            printService.printf(userName + " вы не прошли тест.");
        } else {
            printService.printf(userName + " вы успешно прошли тест.");
        }
    }
}
