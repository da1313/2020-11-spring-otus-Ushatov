package ru.otus.homework.util;

import org.course.homework.service.interfaces.CsvParser;
import org.course.homework.service.CsvParserSimple;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.course.homework.domain.Answer;
import org.course.homework.domain.Question;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Класс CsvParser")
class CsvParserTest {

    public static final int ANSWERS_COUNT = 5;
    private final CsvParser parser = new CsvParserSimple(ANSWERS_COUNT);

    @DisplayName("При передаче пустой строки кидается исключение")
    @Test
    void throwsIfEmptyLine() {
        String line = "";
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> parser.parse(line));
        Assertions.assertEquals("Parse error. Empty line or insufficient delimiters. " + line, exception.getMessage());
    }

    @DisplayName("При передаче строки с неверным количеством разделителей кидается исключение")
    @Test
    void throwsIfInsufficientDelimiters() {
        String line = "Question 1,Answer 11,Answer 12,Answer 13,Answer 14,Answer 15,1,1,0,";
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> parser.parse(line));
        Assertions.assertEquals("Parse error. Empty line or insufficient delimiters. " + line, exception.getMessage());
    }

    @DisplayName("Содержание вопроса считанно верно")
    @Test
    void getQuestionName() {
        String line = "Question 1,Answer 11,Answer 12,Answer 13,Answer 14,Answer 15,1,1,0,0,";
        Question question = parser.parse(line);
        Assertions.assertEquals(question.getQuestionContent(), "Question 1");
    }

    @DisplayName("Содержание ответов считанно верно")
    @Test
    void getQuestionAnswers() {
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