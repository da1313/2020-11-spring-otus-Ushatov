package ru.otus.homework.service;

import org.course.homework.domain.Answer;
import org.course.homework.domain.Question;
import org.course.homework.service.PrintServiceWithStream;
import org.course.homework.service.TestAdvisorServiceImpl;
import org.course.homework.service.interfaces.PrintService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Class TestAdvisorService")
class TestAdvisorServiceImplTest {

    public static final String USER_NAME = "Ivan Ivanov";
    public static final int USER_ANSWERS_1 = 1;
    public static final int USER_ANSWERS_2 = 3;
    public static final int QUESTION_COUNT = 4;

    private final double passRate = 50;
    private PrintService printService;

    @DisplayName("Ответ засчитан как верный")
    @Test
    void validateTrue() {
        List<Answer> answerList = new ArrayList<>();
        Question question = new Question("Question 1", answerList);
        answerList.add(new Answer("Answer 11", question, true));
        answerList.add(new Answer("Answer 12", question, false));
        answerList.add(new Answer("Answer 13", question, true));
        TestAdvisorServiceImpl validateService = new TestAdvisorServiceImpl(printService, passRate);
        boolean validate = validateService.validate(question.getAnswers(), List.of(answerList.get(0), answerList.get(2)));
        assertTrue(validate);
    }

    @DisplayName("Ответ засчитан как не верный")
    @Test
    void validateFalse() {
        List<Answer> answerList = new ArrayList<>();
        Question question = new Question("Question 1", answerList);
        answerList.add(new Answer("Answer 11", question, true));
        answerList.add(new Answer("Answer 12", question, false));
        answerList.add(new Answer("Answer 13", question, true));
        TestAdvisorServiceImpl validateService = new TestAdvisorServiceImpl(printService, passRate);
        boolean validate = validateService.validate(question.getAnswers(), List.of(answerList.get(0), answerList.get(1)));
        assertFalse(validate);
    }


    @DisplayName("Тест не пройден")
    @Test
    void printResultFailure() {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        printService = new PrintServiceWithStream(new PrintStream(buffer), new ByteArrayInputStream(USER_NAME.getBytes()));
        TestAdvisorServiceImpl validateService = new TestAdvisorServiceImpl(printService, passRate);
        validateService.printResult(USER_NAME + " вы не прошли тест.", USER_ANSWERS_1, QUESTION_COUNT);
    }

    @DisplayName("Тест пройден")
    @Test
    void printResultSuccess() {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        printService = new PrintServiceWithStream(new PrintStream(buffer), new ByteArrayInputStream(USER_NAME.getBytes()));
        TestAdvisorServiceImpl validateService = new TestAdvisorServiceImpl(printService, passRate);
        validateService.printResult(USER_NAME + " вы не прошли тест.", USER_ANSWERS_2, QUESTION_COUNT);
    }
}