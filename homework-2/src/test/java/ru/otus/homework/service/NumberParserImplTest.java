package ru.otus.homework.service;

import org.course.homework.service.NumberParserImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Class NumberParserImpl")
class NumberParserImplTest {

    public static final int MAX_QUESTION_COUNT = 5;

    @DisplayName("Коректная работа выбраны все")
    @Test
    void parseAnswerNumbersOnAllNumbers() {
        NumberParserImpl service = new NumberParserImpl();
        String test = "1,2,3,4,5";
        assertIterableEquals(List.of(1,2,3,4,5), service.parseAnswerNumbers(MAX_QUESTION_COUNT, test));
    }

    @DisplayName("Коректная работа выбран один")
    @Test
    void parseAnswerNumbersOnOneNumber() {
        NumberParserImpl service = new NumberParserImpl();
        String test = "1";
        assertIterableEquals(List.of(1), service.parseAnswerNumbers(MAX_QUESTION_COUNT, test));
    }

    @DisplayName("Возвращяет null при пустой строке")
    @Test
    void parseAnswerNumbersOnEmptyString() {
        NumberParserImpl service = new NumberParserImpl();
        String test = "";
        assertIterableEquals(null, service.parseAnswerNumbers(MAX_QUESTION_COUNT, test));
    }

    @DisplayName("Возвращяет null при количестве ответов больше максимума")
    @Test
    void parseAnswerNumbersOnOverNumber() {
        NumberParserImpl service = new NumberParserImpl();
        String test = "1,2,3,4,5,6";
        assertIterableEquals(null, service.parseAnswerNumbers(MAX_QUESTION_COUNT, test));
    }

}