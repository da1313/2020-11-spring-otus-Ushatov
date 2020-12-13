package org.course.homework.service;

import org.course.homework.config.TestProperties;
import org.course.homework.domain.Answer;
import org.course.homework.domain.Question;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Class CsvParserSimple")
class CsvParserSimpleTest {

    public static final int MAX_ANSWERS_COUNT = 5;
    public static final int TEST_QUESTION_COUNT = 5;
    public static final int PASS_RATE = 50;
    public static final String CSV_NAME_PREFIX = "questions";
    public static final String CSV_LOCATION = "questions";
    public static final String LOCALE_NAME_PREFIX = "bundle";
    public static final String LOCALE_LOCATION = "";
    public static final Locale LOCALE = new Locale("ru");

    public TestProperties testProperties;

    @BeforeEach
    void initProperties(){
        testProperties = new TestProperties(
                LOCALE,
                CSV_NAME_PREFIX,
                CSV_LOCATION,
                LOCALE_NAME_PREFIX,
                LOCALE_LOCATION,
                MAX_ANSWERS_COUNT,
                TEST_QUESTION_COUNT,
                PASS_RATE);
    }

    @DisplayName("При передаче пустой строки кидается исключение")
    @Test
    void throwsIfEmptyLine() {
        CsvParserSimple parser = new CsvParserSimple(testProperties);
        String line = "";
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> parser.parse(line));
        Assertions.assertEquals("Parse error. Empty line or insufficient delimiters. " + line, exception.getMessage());
    }

    @DisplayName("При передаче строки с неверным количеством разделителей кидается исключение")
    @Test
    void throwsIfInsufficientDelimiters() {
        CsvParserSimple parser = new CsvParserSimple(testProperties);
        String line = "Question 1,Answer 11,Answer 12,Answer 13,Answer 14,Answer 15,1,1,0,";
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> parser.parse(line));
        Assertions.assertEquals("Parse error. Empty line or insufficient delimiters. " + line, exception.getMessage());
    }

    @DisplayName("Содержание вопроса считанно верно")
    @Test
    void getQuestionName() {
        CsvParserSimple parser = new CsvParserSimple(testProperties);
        String line = "Question 1,Answer 11,Answer 12,Answer 13,Answer 14,Answer 15,1,1,0,0,";
        Question question = parser.parse(line);
        Assertions.assertEquals(question.getQuestionContent(), "Question 1");
    }

    @DisplayName("Содержание ответов считанно верно")
    @Test
    void getQuestionAnswers() {
        CsvParserSimple parser = new CsvParserSimple(testProperties);
        String line = "Question content,Answer 1,Answer 2,,,,0,1,,,";
        Question question = parser.parse(line);
        List<Answer> answers = question.getAnswers();
        Assertions.assertAll(
                () -> assertEquals(answers.get(0).getAnswerContent(), "Answer 1"),
                () -> assertFalse(answers.get(0).isCorrect()),
                () -> assertEquals(answers.get(1).getAnswerContent(), "Answer 2"),
                () -> assertTrue(answers.get(1).isCorrect()));
    }
}