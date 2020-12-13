package org.course.homework.dao;

import org.course.homework.config.TestProperties;
import org.course.homework.domain.Answer;
import org.course.homework.domain.Question;
import org.course.homework.service.interfaces.CsvParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Класс QuestionDaoLocale")
@ExtendWith(MockitoExtension.class)
class QuestionDaoLocaleTest {
    @Mock
    private CsvParser parser;
    @Mock
    private TestProperties testProperties;
    @Mock
    private MessageSource messageSource;

    private Map<String, Question> questionList;
    public static final int TEST_FILE_QUESTIONS_COUNT = 5;

    public static final int MAX_ANSWERS_COUNT = 5;
    public static final int TEST_QUESTION_COUNT = 5;
    public static final double PASS_RATE = 50;
    public static final String CSV_NAME_PREFIX = "questions";
    public static final String CSV_LOCATION = "questions";
    public static final String LOCALE_NAME_PREFIX = "bundle";
    public static final String LOCALE_LOCATION = "";
    public static final Locale LOCALE = new Locale("en");

    @BeforeEach
    void initProperties() {
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

    @BeforeEach
    public void prepareQuestions() {
        questionList = new HashMap<>();
        List<Answer> answers1 = new ArrayList<>();
        Question question1 = new Question("Question 1", answers1);
        answers1.add(new Answer("Answer 11", question1, true));
        answers1.add(new Answer("Answer 12", question1, true));
        answers1.add(new Answer("Answer 13", question1, false));
        answers1.add(new Answer("Answer 14", question1, false));
        answers1.add(new Answer("Answer 15", question1, false));
        questionList.put("Question 1,Answer 11,Answer 12,Answer 13,Answer 14,Answer 15,1,1,0,0,0", question1);
        List<Answer> answers2 = new ArrayList<>();
        Question question2 = new Question("Question 2", answers2);
        answers2.add(new Answer("Answer 21", question2, true));
        answers2.add(new Answer("Answer 22", question2, false));
        questionList.put("Question 2,Answer 21,Answer 22,,,,1,0,,,", question2);
        List<Answer> answers3 = new ArrayList<>();
        Question question3 = new Question("Question 3", answers3);
        answers3.add(new Answer("Answer 31", question3, true));
        answers3.add(new Answer("Answer 32", question3, false));
        answers3.add(new Answer("Answer 33", question3, false));
        questionList.put("Question 3,Answer 31,Answer 32,Answer 33,,,1,0,0,,", question3);
        List<Answer> answers4 = new ArrayList<>();
        Question question4 = new Question("Question 4", answers4);
        answers4.add(new Answer("Answer 41", question4, false));
        answers4.add(new Answer("Answer 42", question4, true));
        questionList.put("Question 4,Answer 41,Answer 42,,,,0,1,,,", question4);
        List<Answer> answers5 = new ArrayList<>();
        Question question5 = new Question("Question 5", answers5);
        answers5.add(new Answer("Answer 51", question5, false));
        answers5.add(new Answer("Answer 52", question5, true));
        answers5.add(new Answer("Answer 53", question5, false));
        answers5.add(new Answer("Answer 54", question5, true));
        questionList.put("Question 5,Answer 51,Answer 52,Answer 53,Answer 54,,0,1,0,1,", question5);
    }

    @DisplayName("При неправильном имени файла кидается исключение")
    @Test
    void throwsExceptionsIfFileNotFound() {
        int maxAnswersCount = 5;
        int testQuestionsCount = 5;
        int passRate = 50;
        String csvNamePrefix = "questions";
        String csvLocation = "test1";
        String localeNamePrefix = "bundle";
        String localeLocation = "";
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
        String fileName = localeNamePrefix + "_" + locale + ".properties";
        String line1 = "Question 1,Answer 11,Answer 12,Answer 13,Answer 14,Answer 15,1,1,0,0,0";
        Mockito.lenient().when(parser.parse(line1)).thenReturn(questionList.get(line1));
        Mockito.lenient().when(messageSource.getMessage("question.1", new String[]{}, testProperties.getLocale())).thenReturn(line1);
        QuestionDao questionDaoCsv = new QuestionDaoLocale(testProperties, messageSource, parser);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, questionDaoCsv::getAllQuestions);
        assertEquals("File not found! " + fileName, exception.getMessage());
    }

    @DisplayName("Количество вопросов в файле равно количеству считанных")
    @Test
    void getAllQuestionsCount() {
        String line1 = "Question 1,Answer 11,Answer 12,Answer 13,Answer 14,Answer 15,1,1,0,0,0";
        Mockito.lenient().when(parser.parse(line1)).thenReturn(questionList.get(line1));
        String line2 = "Question 2,Answer 21,Answer 22,,,,1,0,,,";
        Mockito.lenient().when(parser.parse(line2)).thenReturn(questionList.get(line2));
        String line3 = "Question 3,Answer 31,Answer 32,Answer 33,,,1,0,0,,";
        Mockito.lenient().when(parser.parse(line3)).thenReturn(questionList.get(line3));
        String line4 = "Question 4,Answer 41,Answer 42,,,,0,1,,,";
        Mockito.lenient().when(parser.parse(line4)).thenReturn(questionList.get(line4));
        String line5 = "Question 5,Answer 51,Answer 52,Answer 53,Answer 54,,0,1,0,1,";
        Mockito.lenient().when(parser.parse(line5)).thenReturn(questionList.get(line5));
        Mockito.lenient().when(messageSource.getMessage("question.1", new String[]{}, testProperties.getLocale())).thenReturn(line1);
        Mockito.lenient().when(messageSource.getMessage("question.2", new String[]{}, testProperties.getLocale())).thenReturn(line2);
        Mockito.lenient().when(messageSource.getMessage("question.3", new String[]{}, testProperties.getLocale())).thenReturn(line3);
        Mockito.lenient().when(messageSource.getMessage("question.4", new String[]{}, testProperties.getLocale())).thenReturn(line4);
        Mockito.lenient().when(messageSource.getMessage("question.5", new String[]{}, testProperties.getLocale())).thenReturn(line5);
        QuestionDao questionDaoCsv = new QuestionDaoLocale(testProperties, messageSource, parser);
        List<Question> allQuestions = questionDaoCsv.getAllQuestions();
        assertEquals(TEST_FILE_QUESTIONS_COUNT, allQuestions.size());
    }

    @DisplayName("Содержание всех вопросов считано верно")
    @Test
    void getAllQuestionsNames() {
        String line1 = "Question 1,Answer 11,Answer 12,Answer 13,Answer 14,Answer 15,1,1,0,0,0";
        Mockito.lenient().when(parser.parse(line1)).thenReturn(questionList.get(line1));
        String line2 = "Question 2,Answer 21,Answer 22,,,,1,0,,,";
        Mockito.lenient().when(parser.parse(line2)).thenReturn(questionList.get(line2));
        String line3 = "Question 3,Answer 31,Answer 32,Answer 33,,,1,0,0,,";
        Mockito.lenient().when(parser.parse(line3)).thenReturn(questionList.get(line3));
        String line4 = "Question 4,Answer 41,Answer 42,,,,0,1,,,";
        Mockito.lenient().when(parser.parse(line4)).thenReturn(questionList.get(line4));
        String line5 = "Question 5,Answer 51,Answer 52,Answer 53,Answer 54,,0,1,0,1,";
        Mockito.lenient().when(parser.parse(line5)).thenReturn(questionList.get(line5));
        QuestionDao questionDao = new QuestionDaoLocale(testProperties, messageSource, parser);
        Mockito.lenient().when(messageSource.getMessage("question.1", new String[]{}, testProperties.getLocale())).thenReturn(line1);
        Mockito.lenient().when(messageSource.getMessage("question.2", new String[]{}, testProperties.getLocale())).thenReturn(line2);
        Mockito.lenient().when(messageSource.getMessage("question.3", new String[]{}, testProperties.getLocale())).thenReturn(line3);
        Mockito.lenient().when(messageSource.getMessage("question.4", new String[]{}, testProperties.getLocale())).thenReturn(line4);
        Mockito.lenient().when(messageSource.getMessage("question.5", new String[]{}, testProperties.getLocale())).thenReturn(line5);
        List<Question> allQuestions = questionDao.getAllQuestions();
        assertAll(
                () -> assertEquals(questionList.get(line1).getQuestionContent(), allQuestions.get(0).getQuestionContent()),
                () -> assertEquals(questionList.get(line2).getQuestionContent(), allQuestions.get(1).getQuestionContent()),
                () -> assertEquals(questionList.get(line3).getQuestionContent(), allQuestions.get(2).getQuestionContent()),
                () -> assertEquals(questionList.get(line4).getQuestionContent(), allQuestions.get(3).getQuestionContent()),
                () -> assertEquals(questionList.get(line5).getQuestionContent(), allQuestions.get(4).getQuestionContent())
        );
    }

    @DisplayName("Содержание всех ответов верное")
    @Test
    void getAllQuestionsAnswersNames() {
        String line1 = "Question 1,Answer 11,Answer 12,Answer 13,Answer 14,Answer 15,1,1,0,0,0";
        Mockito.lenient().when(parser.parse(line1)).thenReturn(questionList.get(line1));
        String line2 = "Question 2,Answer 21,Answer 22,,,,1,0,,,";
        Mockito.lenient().when(parser.parse(line2)).thenReturn(questionList.get(line2));
        String line3 = "Question 3,Answer 31,Answer 32,Answer 33,,,1,0,0,,";
        Mockito.lenient().when(parser.parse(line3)).thenReturn(questionList.get(line3));
        String line4 = "Question 4,Answer 41,Answer 42,,,,0,1,,,";
        Mockito.lenient().when(parser.parse(line4)).thenReturn(questionList.get(line4));
        String line5 = "Question 5,Answer 51,Answer 52,Answer 53,Answer 54,,0,1,0,1,";
        Mockito.lenient().when(parser.parse(line5)).thenReturn(questionList.get(line5));
        Mockito.lenient().when(messageSource.getMessage("question.1", new String[]{}, testProperties.getLocale())).thenReturn(line1);
        Mockito.lenient().when(messageSource.getMessage("question.2", new String[]{}, testProperties.getLocale())).thenReturn(line2);
        Mockito.lenient().when(messageSource.getMessage("question.3", new String[]{}, testProperties.getLocale())).thenReturn(line3);
        Mockito.lenient().when(messageSource.getMessage("question.4", new String[]{}, testProperties.getLocale())).thenReturn(line4);
        Mockito.lenient().when(messageSource.getMessage("question.5", new String[]{}, testProperties.getLocale())).thenReturn(line5);
        QuestionDao questionDao = new QuestionDaoLocale(testProperties, messageSource, parser);
        List<Question> allQuestions = questionDao.getAllQuestions();
        assertAll(
                () -> assertIterableEquals(questionList.get(line1).getAnswers(), allQuestions.get(0).getAnswers()),
                () -> assertIterableEquals(questionList.get(line2).getAnswers(), allQuestions.get(1).getAnswers()),
                () -> assertIterableEquals(questionList.get(line3).getAnswers(), allQuestions.get(2).getAnswers()),
                () -> assertIterableEquals(questionList.get(line4).getAnswers(), allQuestions.get(3).getAnswers()),
                () -> assertIterableEquals(questionList.get(line5).getAnswers(), allQuestions.get(4).getAnswers())
        );
    }
}