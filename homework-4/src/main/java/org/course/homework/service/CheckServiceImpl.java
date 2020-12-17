package org.course.homework.service;

import org.course.homework.domain.Answer;
import org.course.homework.service.interfaces.CheckService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CheckServiceImpl implements CheckService {
    @Override
    public boolean checkAnswer(List<Answer> userAnswers, List<Answer> correctAnswers) {
        long correctAnswersCount = correctAnswers.stream().filter(Answer::isCorrect).count();
        return userAnswers.size() == correctAnswersCount && userAnswers.stream().allMatch(Answer::isCorrect);
    }

    @Override
    public double getUserRate(int userValidAnswers, int questionCount) {
        return  ((double) userValidAnswers / questionCount) * 100;
    }
}
