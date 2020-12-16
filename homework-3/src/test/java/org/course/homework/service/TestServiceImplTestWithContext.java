package org.course.homework.service;

import org.course.homework.config.TestProperties;
import org.course.homework.dao.QuestionDao;
import org.course.homework.dao.QuestionDaoCsv;
import org.course.homework.domain.Answer;
import org.course.homework.domain.Question;
import org.course.homework.service.interfaces.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
class TestServiceImplTestWithContext {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private CheckService checkService;
    @MockBean
    private UserInterface userInterface;
    @Autowired
    private TestProperties testProperties;

    public static final String USER_NAME = "Ivan";

    @Captor
    private ArgumentCaptor<String> userName;
    @Captor
    private ArgumentCaptor<Double> userRate;
    @Captor
    private ArgumentCaptor<Double> successRate;

    @DisplayName("Тест 100 правильных")
    @Test
    void makeTestAllCorrect(){
        CsvParser parser = new CsvParserSimple(testProperties);
        LocaleResourceLocator resourceLocator = new LocaleResourceLocatorImpl(testProperties);
        QuestionDao questionDao = new QuestionDaoCsv(resourceLocator, parser);
        RandomGenerator randomGenerator = new RandomGeneratorImpl(questionDao);
        QuestionService questionService = new QuestionServiceImpl(randomGenerator);
        CheckService checkService = new CheckServiceImpl();


        Mockito.lenient().when(userInterface.readUserName()).thenReturn(USER_NAME);

        List<Question> questionList = questionService.getQuestions(testProperties.getTestQuestionCount());
        for (int i = 0; i < testProperties.getTestQuestionCount(); i++) {
            Mockito.when(userInterface.readUserAnswers(questionList.get(i)))
                    .thenReturn(questionList.get(i).getAnswers().stream().filter(Answer::isCorrect).collect(Collectors.toList()));
        }

        TestService testService = new TestServiceImpl(questionService, checkService, userInterface, testProperties);
        testService.run();


        Mockito.verify(userInterface).printResult(userName.capture(), userRate.capture(), successRate.capture());
        Assertions.assertEquals(100, userRate.getValue());
    }

    @DisplayName("Тест один неправильный")
    @Test
    void makeTestOneWrong(){
        int count = testProperties.getTestQuestionCount();
        Mockito.lenient().when(userInterface.readUserName()).thenReturn(USER_NAME);

        List<Question> questionList = questionService.getQuestions(count);
        for (int i = 0; i < count - 1; i++) {
            Mockito.when(userInterface.readUserAnswers(questionList.get(i)))
                    .thenReturn(questionList.get(i).getAnswers().stream().filter(Answer::isCorrect).collect(Collectors.toList()));
        }
        Mockito.when(userInterface.readUserAnswers(questionList.get(count - 1)))
                .thenReturn(questionList.get(count - 1)
                        .getAnswers().stream().filter(a -> !a.isCorrect()).collect(Collectors.toList()));

        TestService testService = new TestServiceImpl(questionService, checkService, userInterface, testProperties);
        testService.run();


        Mockito.verify(userInterface).printResult(userName.capture(), userRate.capture(), successRate.capture());
        Assertions.assertEquals((((double) count - 1) / count) * 100 , userRate.getValue());
    }
}