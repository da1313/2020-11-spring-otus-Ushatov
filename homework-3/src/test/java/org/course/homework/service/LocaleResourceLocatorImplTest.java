package org.course.homework.service;

import org.course.homework.config.TestProperties;
import org.course.homework.service.interfaces.LocaleResourceLocator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Class LocaleResourceLocatorImpl")
class LocaleResourceLocatorImplTest {

    @DisplayName("Ресурсы успешно найдены")
    @Test
    void resourcesCorrectlyFounded() {
        int maxAnswersCount = 5;
        int testQuestionsCount = 5;
        int passRate = 50;
        String csvNamePrefix = "questions";
        String csvLocation = "test1";
        String localeNamePrefix = "bundle";
        String localeLocation = "test1";
        Locale locale = new Locale("en");
        TestProperties testProperties = new TestProperties(
                locale,
                csvNamePrefix,
                csvLocation,
                localeNamePrefix,
                localeLocation,
                maxAnswersCount,
                testQuestionsCount,
                passRate);
        LocaleResourceLocator resourceLocator = new LocaleResourceLocatorImpl(testProperties);
        String resource = resourceLocator.getResource();
        assertEquals("test1/questions_en.csv", resource);
    }

    @DisplayName("Ресурсы не найдены но найден ресурс по умолчанию")
    @Test
    void foundDefaultRecourse() {
        int maxAnswersCount = 5;
        int testQuestionsCount = 5;
        int passRate = 50;
        String csvNamePrefix = "questions";
        String csvLocation = "test2";
        String localeNamePrefix = "bundle";
        String localeLocation = "test2";
        Locale locale = new Locale("nd");
        TestProperties testProperties = new TestProperties(
                locale,
                csvNamePrefix,
                csvLocation,
                localeNamePrefix,
                localeLocation,
                maxAnswersCount,
                testQuestionsCount,
                passRate);
        String path = csvLocation + "/" + csvNamePrefix + "_" + Locale.getDefault().getLanguage() + ".csv";
        LocaleResourceLocator resourceLocator = new LocaleResourceLocatorImpl(testProperties);
        String resource = resourceLocator.getResource();
        assertEquals(path, resource);
    }

    @DisplayName("Если никакие ресурсы не найдены кидается исключение")
    @Test
    void recourseNotFound() {
        int maxAnswersCount = 5;
        int testQuestionsCount = 5;
        int passRate = 50;
        String csvNamePrefix = "questions";
        String csvLocation = "test3";
        String localeNamePrefix = "bundle";
        String localeLocation = "test3";
        Locale locale = new Locale("en");
        TestProperties testProperties = new TestProperties(
                locale,
                csvNamePrefix,
                csvLocation,
                localeNamePrefix,
                localeLocation,
                maxAnswersCount,
                testQuestionsCount,
                passRate);
        LocaleResourceLocator resourceLocator = new LocaleResourceLocatorImpl(testProperties);
        String path = csvLocation + "/" + csvNamePrefix + "_" + Locale.getDefault().getLanguage() + ".csv";
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, resourceLocator::getResource);
        assertEquals("Can't find default question file! " + path, exception.getMessage());
    }

    @DisplayName("Ресурс не найден, а у ресурса по умолчанию нет локализации")
    @Test
    void recourseNotFoundDefaultWithoutLocalization() {
        int maxAnswersCount = 5;
        int testQuestionsCount = 5;
        int passRate = 50;
        String csvNamePrefix = "questions";
        String csvLocation = "test4";
        String localeNamePrefix = "bundle";
        String localeLocation = "test4";
        Locale locale = new Locale("en");
        TestProperties testProperties = new TestProperties(
                locale,
                csvNamePrefix,
                csvLocation,
                localeNamePrefix,
                localeLocation,
                maxAnswersCount,
                testQuestionsCount,
                passRate);
        LocaleResourceLocator resourceLocator = new LocaleResourceLocatorImpl(testProperties);
        String path = localeLocation + "/" + localeNamePrefix + "_" + Locale.getDefault().getLanguage() + ".properties";
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, resourceLocator::getResource);
        assertEquals("Can't find default localization file! " + path, exception.getMessage());
    }

    @DisplayName("Ресурс найден, но у него нет локализации, тогда используем по умолчанию")
    @Test
    void recourseNotFound1() {
        int maxAnswersCount = 5;
        int testQuestionsCount = 5;
        int passRate = 50;
        String csvNamePrefix = "questions";
        String csvLocation = "test5";
        String localeNamePrefix = "bundle";
        String localeLocation = "test5";
        Locale locale = new Locale("en");
        TestProperties testProperties = new TestProperties(
                locale,
                csvNamePrefix,
                csvLocation,
                localeNamePrefix,
                localeLocation,
                maxAnswersCount,
                testQuestionsCount,
                passRate);
        LocaleResourceLocator resourceLocator = new LocaleResourceLocatorImpl(testProperties);
        String path = csvLocation + "/" + csvNamePrefix + "_" + Locale.getDefault().getLanguage() + ".csv";
        String resource = resourceLocator.getResource();
        assertEquals(path, resource);
    }
}