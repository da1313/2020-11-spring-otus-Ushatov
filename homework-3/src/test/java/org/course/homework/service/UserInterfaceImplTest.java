package org.course.homework.service;

import org.course.homework.config.TestProperties;
import org.course.homework.domain.Answer;
import org.course.homework.domain.Question;
import org.course.homework.service.interfaces.NumberParser;
import org.course.homework.service.interfaces.PrintService;
import org.course.homework.service.interfaces.RandomGenerator;
import org.course.homework.service.interfaces.UserInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

@DisplayName("Class UserInterfaceImpl")
@ExtendWith(MockitoExtension.class)
class UserInterfaceImplTest {

    @Mock
    private RandomGenerator randomGenerator;
    @Mock
    private NumberParser numberParser;
    @Mock
    private MessageSource messageSource;
    private TestProperties testProperties;
    private PrintService printService;

    private List<Question> questionList;

    public static final int QUESTION_COUNT = 3;
    public static final int QUESTION_NUMBER = 666;

    public static final int MAX_ANSWERS_COUNT = 5;
    public static final int TEST_QUESTION_COUNT = 5;
    public static final double PASS_RATE = 50;
    public static final Locale LOCALE = new Locale("ru");
    public static final String CSV_FILE_NAME = "questions.csv";

    public static final String USER_INPUT_1 = "1,3";
    public static final String USER_INPUT_2 = "5";
    public static final String USER_NAME = "Ivan Ivanov";

    public static final double USER_PASS_RATE = 40;

    @BeforeEach
    void questionInit(){
        questionList = new ArrayList<>();
        List<Answer> answerList = new ArrayList<>();
        Question question = new Question("Question 1", answerList);
        answerList.add(new Answer("Answer 11", question, true));
        answerList.add(new Answer("Answer 12", question, true));
        answerList.add(new Answer("Answer 13", question, true));
        questionList.add(question);
        List<Answer> answerList1 = new ArrayList<>();
        Question question1 = new Question("Question 2", answerList1);
        answerList1.add(new Answer("Answer 21", question, true));
        answerList1.add(new Answer("Answer 22", question, true));
        answerList1.add(new Answer("Answer 23", question, true));
        questionList.add(question1);
        List<Answer> answerList2 = new ArrayList<>();
        Question question2 = new Question("Question 3", answerList2);
        answerList2.add(new Answer("Answer 21", question, true));
        answerList2.add(new Answer("Answer 22", question, true));
        answerList2.add(new Answer("Answer 23", question, true));
        questionList.add(question2);
    }

    @BeforeEach
    void initProperties(){
        testProperties = new TestProperties(
                LOCALE,
                MAX_ANSWERS_COUNT,
                TEST_QUESTION_COUNT,
                PASS_RATE,
                CSV_FILE_NAME);
    }

    @Test
    void printQuestion() {
        Mockito.lenient().when(randomGenerator.generate(QUESTION_COUNT)).thenReturn(questionList);
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        printService = new PrintServiceWithStream(new PrintStream(buffer), new ByteArrayInputStream("in".getBytes()));
        UserInterface service = new UserInterfaceImpl(
                printService,
                numberParser,
                messageSource,
                testProperties
                );
        service.printQuestion(questionList.get(0), QUESTION_NUMBER);
        assertEquals("666 Question 1\n", new String(buffer.toByteArray()));
    }

    @Test
    void printAnswers() {
        Mockito.lenient().when(randomGenerator.generate(QUESTION_COUNT)).thenReturn(questionList);
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        printService = new PrintServiceWithStream(new PrintStream(buffer), new ByteArrayInputStream("in".getBytes()));
        UserInterface service = new UserInterfaceImpl(
                printService,
                numberParser,
                messageSource,
                testProperties
        );
        service.printAnswers(questionList.get(0).getAnswers());
        assertEquals("\t1 Answer 11\n\t2 Answer 12\n\t3 Answer 13\n", new String(buffer.toByteArray()));
    }

    @DisplayName("Ответы считываются верно")
    @Test
    void readUserAnswers() {
        List<Answer> answerList = new ArrayList<>();
        Question question = new Question("Question 1", answerList);
        answerList.add(new Answer("Answer 11", question, true));
        answerList.add(new Answer("Answer 12", question, true));
        answerList.add(new Answer("Answer 13", question, true));
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        printService = new PrintServiceWithStream(new PrintStream(buffer), new ByteArrayInputStream(USER_INPUT_1.getBytes()));
        Mockito.lenient().when(numberParser.parseAnswerNumbers(question.getAnswers().size(), USER_INPUT_1)).thenReturn(List.of(1, 3));
        UserInterface service = new UserInterfaceImpl(
                printService,
                numberParser,
                messageSource,
                testProperties
        );
        List<Answer> userAnswers = service.readUserAnswers(question);
        assertIterableEquals(List.of(question.getAnswers().get(0), question.getAnswers().get(2)), userAnswers);
    }

    @DisplayName("Если выбран номер которого нет выводится сообщение")
    @Test
    void readIncorrectNumber() {
        List<Answer> answerList = new ArrayList<>();
        Question question = new Question("Question 1", answerList);
        answerList.add(new Answer("Answer 11", question, true));
        answerList.add(new Answer("Answer 12", question, true));
        answerList.add(new Answer("Answer 13", question, true));
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        printService = new PrintServiceWithStream(new PrintStream(buffer), new ByteArrayInputStream(USER_INPUT_2.getBytes()));
        Mockito.lenient().when(numberParser.parseAnswerNumbers(question.getAnswers().size(), USER_INPUT_2)).thenReturn(null);
        Mockito.lenient().when(messageSource.getMessage("user.enter.question-number.incorrect", new String[]{}, testProperties.getLocale())).thenReturn("test message");
        UserInterface service = new UserInterfaceImpl(
                printService,
                numberParser,
                messageSource,
                testProperties
        );
        try{
            List<Answer> userAnswers = service.readUserAnswers(question);
        }catch (Exception e){
            assertEquals("test message\n", new String(buffer.toByteArray()));
        }
    }

    @DisplayName("Имя пользователя считывается корректно")
    @Test
    void readUserName() {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        printService = new PrintServiceWithStream(new PrintStream(buffer), new ByteArrayInputStream(USER_NAME.getBytes()));
        UserInterface service = new UserInterfaceImpl(
                printService,
                numberParser,
                messageSource,
                testProperties
        );
        String userName = service.readUserName();
        assertEquals(USER_NAME, userName);
    }

    @Test
    void printResult() {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        printService = new PrintServiceWithStream(new PrintStream(buffer), new ByteArrayInputStream(USER_NAME.getBytes()));
        UserInterface service = new UserInterfaceImpl(
                printService,
                numberParser,
                messageSource,
                testProperties
        );
        Mockito.lenient().when(messageSource.getMessage("test.output.result.false", new String[]{USER_NAME}, testProperties.getLocale())).thenReturn("false");
        Mockito.lenient().when(messageSource.getMessage("test.output.result.true", new String[]{USER_NAME}, testProperties.getLocale())).thenReturn("true");
        service.printResult(USER_NAME, USER_PASS_RATE, PASS_RATE);
        assertEquals("false", new String(buffer.toByteArray()));
    }
}