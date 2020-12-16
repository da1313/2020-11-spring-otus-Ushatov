package org.course.homework.service;

import org.course.homework.config.TestProperties;
import org.course.homework.dao.QuestionDao;
import org.course.homework.dao.QuestionDaoCsv;
import org.course.homework.domain.Answer;
import org.course.homework.domain.Question;
import org.course.homework.service.interfaces.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@DisplayName("Class TestServiceImpl")
@ExtendWith(MockitoExtension.class)
class TestServiceImplTest {

    @Mock
    private UserInterface userInterface;
    private TestProperties testProperties;

    public static final int MAX_ANSWERS_COUNT = 5;
    public static final int TEST_QUESTION_COUNT = 5;
    public static final int PASS_RATE = 50;
    public static final Locale LOCALE = new Locale("ru");
    public static final String CSV_FILE_NAME = "test1/questions.csv";

    public static final String USER_NAME = "Ivan";

    @Captor
    private ArgumentCaptor<String> userName;
    @Captor
    private ArgumentCaptor<Double> userRate;
    @Captor
    private ArgumentCaptor<Double> successRate;

    @BeforeEach
    void initProperties(){
        testProperties = new TestProperties(
                LOCALE,
                MAX_ANSWERS_COUNT,
                TEST_QUESTION_COUNT,
                PASS_RATE,
                CSV_FILE_NAME);
    }

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
        CsvParser parser = new CsvParserSimple(testProperties);
        LocaleResourceLocator resourceLocator = new LocaleResourceLocatorImpl(testProperties);
        QuestionDao questionDao = new QuestionDaoCsv(resourceLocator, parser);
        RandomGenerator randomGenerator = new RandomGeneratorImpl(questionDao);
        QuestionService questionService = new QuestionServiceImpl(randomGenerator);
        CheckService checkService = new CheckServiceImpl();
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