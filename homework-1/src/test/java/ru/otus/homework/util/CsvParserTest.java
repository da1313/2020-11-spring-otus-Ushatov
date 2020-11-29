package ru.otus.homework.util;

import org.course.homework.util.CsvParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.course.homework.domain.Answer;
import org.course.homework.domain.Question;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Класс CsvParser")
class CsvParserTest {
    @DisplayName("При отсуствии вопроса или ответов кидается исключение")
    @Test
    void throwsIfEmptyAnswer() {
        String line = "Q1,";
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> CsvParser.parse(line));
        Assertions.assertEquals("Присуствует вопрос без ответа", exception.getMessage());
    }

    @DisplayName("Содержание вопроса считанно верно")
    @Test
    void getQuestionName() {
        String line = "Question content,Answer 1,Answer 2";
        Question question = CsvParser.parse(line);
        Assertions.assertEquals(question.getContent(), "Question content");
    }

    @DisplayName("Содержание ответов считанно верно")
    @Test
    void getQuestionAnswers() {
        String line = "Question content,Answer 1,Answer 2";
        Question question = CsvParser.parse(line);
        List<Answer> answers = question.getAnswers();
        Assertions.assertAll(
                () -> assertEquals(answers.get(0).getContent(), "Answer 1"),
                () -> assertEquals(answers.get(1).getContent(), "Answer 2"));
    }
}