package org.course.homework.service;

import org.course.homework.config.TestProperties;
import org.course.homework.service.interfaces.LocaleResourceLocator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Class LocaleResourceLocatorImpl")
class LocaleResourceLocatorImplTest {

    @DisplayName("Ресурсы успешно найдены")
    @Test
    void resourcesCorrectlyFounded() {
        int maxAnswersCount = 5;
        int testQuestionsCount = 5;
        int passRate = 50;
        String csvFileName = "test1/questions.csv";
        Locale locale = new Locale("en");
        TestProperties testProperties = new TestProperties(
                locale,
                maxAnswersCount,
                testQuestionsCount,
                passRate,
                csvFileName
                );
        LocaleResourceLocator resourceLocator = new LocaleResourceLocatorImpl(testProperties);
        String resource = resourceLocator.getResource();
        assertEquals("test1/questions_en.csv", resource);
    }

    @DisplayName("Ресурсы не найдены использован файл по умолчанию")
    @Test
    void resourcesNotFound() {
        int maxAnswersCount = 5;
        int testQuestionsCount = 5;
        int passRate = 50;
        String csvFileName = "test1/questions.csv";
        Locale locale = new Locale("fn");
        TestProperties testProperties = new TestProperties(
                locale,
                maxAnswersCount,
                testQuestionsCount,
                passRate,
                csvFileName
        );
        LocaleResourceLocator resourceLocator = new LocaleResourceLocatorImpl(testProperties);
        String resource = resourceLocator.getResource();
        assertEquals("test1/questions.csv", resource);
    }

}