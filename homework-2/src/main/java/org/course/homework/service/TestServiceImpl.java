package org.course.homework.service;

import org.course.homework.domain.Answer;
import org.course.homework.domain.Question;
import org.course.homework.service.interfaces.QuestionService;
import org.course.homework.service.interfaces.TestAdvisorService;
import org.course.homework.service.interfaces.TestService;
import org.course.homework.service.interfaces.UserService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class TestServiceImpl implements TestService {

    private final QuestionService questionService;
    private final UserService userService;
    private final TestAdvisorService testAdvisorService;

    public TestServiceImpl(QuestionService questionService, UserService userService, TestAdvisorService validateService) {
        this.questionService = questionService;
        this.userService = userService;
        this.testAdvisorService = validateService;
    }

    @Override
    public void run(){
        int count = 0;
        String userName = userService.readUserName();
        List<Question> questionList = questionService.getQuestions();
        int questionCount = questionList.size();
        for (int i = 0; i < questionCount; i++) {
            Question question = questionList.get(i);
            List<Answer> answers = question.getAnswers();
            Collections.shuffle(answers);
            questionService.printQuestion(question, i + 1);
            questionService.printAnswers(answers);
            List<Answer> userAnswers = userService.readAnswer(answers);
            boolean validate = testAdvisorService.validate(answers, userAnswers);
            if (validate) {
                count++;
            }
        }
        testAdvisorService.printResult(userName, count, questionCount);
    }
}
