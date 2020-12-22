package org.course.homework.service;

import org.course.homework.domain.Answer;
import org.course.homework.domain.Question;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Class CheckServiceImpl")
class CheckServiceImplTest {

    public static final int VALID_USER_ANSWERS = 3;
    public static final int QUESTION_COUNT = 10;

    @DisplayName("Ответ засчитан как верный")
    @Test
    void validateTrue() {
        List<Answer> answerList = new ArrayList<>();
        Question question = new Question("Question 1", answerList);
        answerList.add(new Answer("Answer 11", question, true));
        answerList.add(new Answer("Answer 12", question, false));
        answerList.add(new Answer("Answer 13", question, true));
        CheckServiceImpl service = new CheckServiceImpl();
        boolean isValid = service.checkAnswer(List.of(answerList.get(0), answerList.get(2)), question.getAnswers());
        assertTrue(isValid);
    }

    @DisplayName("Ответ засчитан как не верный")
    @Test
    void validateFalse() {
        List<Answer> answerList = new ArrayList<>();
        Question question = new Question("Question 1", answerList);
        answerList.add(new Answer("Answer 11", question, true));
        answerList.add(new Answer("Answer 12", question, false));
        answerList.add(new Answer("Answer 13", question, true));
        CheckServiceImpl service = new CheckServiceImpl();
        boolean isValid = service.checkAnswer(List.of(answerList.get(0), answerList.get(1)), question.getAnswers());
        assertFalse(isValid);
    }


    @DisplayName("Задана верная формула")
    @Test
    void validateUserRate() {
        CheckServiceImpl service = new CheckServiceImpl();
        double userRate = service.getUserRate(VALID_USER_ANSWERS, QUESTION_COUNT);
        assertEquals((double) VALID_USER_ANSWERS / QUESTION_COUNT * 100, userRate);
    }

}