package org.course.homework.service;

import lombok.AllArgsConstructor;
import org.course.homework.config.TestProperties;
import org.course.homework.domain.Answer;
import org.course.homework.domain.Question;
import org.course.homework.service.interfaces.*;
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

    @Override
    public void run() {
        int validAnswers = 0;
        int questionCount = testProperties.getTestQuestionCount();
        double passValue = testProperties.getPassRate();
        List<Question> questionList = questionService.getQuestions(questionCount);
        String userName = userInterface.readUserName();
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
        userInterface.printResult(userName, userRate, passValue);
    }
}
