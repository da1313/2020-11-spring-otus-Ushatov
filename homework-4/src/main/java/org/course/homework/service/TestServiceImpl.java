package org.course.homework.service;

import lombok.AllArgsConstructor;
import org.course.homework.aspect.LogMethod;
import org.course.homework.config.TestProperties;
import org.course.homework.domain.Answer;
import org.course.homework.domain.Question;
import org.course.homework.service.interfaces.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class TestServiceImpl implements TestService {

    private final QuestionService questionService;
    private final CheckService checkService;
    private final UserInterface userInterface;
    private final TestProperties testProperties;

    @LogMethod(argsPattern = "User %s start the test", argsOrder = 0)
    @Override
    public double run(String username) {
        int validAnswers = 0;
        int questionCount = testProperties.getTestQuestionCount();
        List<Question> questionList = questionService.getQuestions(questionCount);
        for (int i = 0; i < questionCount; i++) {
            Question question = questionList.get(i);
            List<Answer> answers = question.getAnswers();
            Collections.shuffle(answers);
            userInterface.printQuestion(question, i + 1);
            userInterface.printAnswers(answers);
            List<Answer> userAnswers = userInterface.readUserAnswers(question);
            boolean isValid = checkService.checkAnswer(userAnswers, answers);
            if (isValid) {
                validAnswers++;
            }
        }
        double userRate = checkService.getUserRate(validAnswers, questionCount);
        userInterface.printResult(username, userRate, testProperties.getPassRate());
        return userRate;
    }
}
