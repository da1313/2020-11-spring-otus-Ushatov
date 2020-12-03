package ru.otus.homework.service;

import org.course.homework.domain.Answer;
import org.course.homework.domain.Question;
import org.course.homework.service.*;
import org.course.homework.service.interfaces.NumberParser;
import org.course.homework.service.interfaces.PrintService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Class UserServiceImpl")
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    public static final String USER_NAME = "Ivan Ivanov";
    public static final String USER_INPUT = "1,3";
    private PrintService printService;
    @Mock
    private NumberParser numberParser;

    @Test
    void readUserName() {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        printService = new PrintServiceWithStream(new PrintStream(buffer), new ByteArrayInputStream(USER_NAME.getBytes()));
        UserServiceImpl service = new UserServiceImpl(printService, numberParser);
        String userName = service.readUserName();
        assertEquals(USER_NAME, userName);
    }

    @Test
    void readAnswer() {
        List<Answer> answerList = new ArrayList<>();
        Question question = new Question("Question 1", answerList);
        answerList.add(new Answer("Answer 11", question, true));
        answerList.add(new Answer("Answer 12", question, true));
        answerList.add(new Answer("Answer 13", question, true));
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        printService = new PrintServiceWithStream(new PrintStream(buffer), new ByteArrayInputStream(USER_INPUT.getBytes()));
        Mockito.lenient().when(numberParser.parseAnswerNumbers(question.getAnswers().size(), USER_INPUT)).thenReturn(List.of(1, 3));
        UserServiceImpl service = new UserServiceImpl(printService, numberParser);
        List<Answer> userAnswers = service.readAnswer(question.getAnswers());
        assertIterableEquals(List.of(question.getAnswers().get(0), question.getAnswers().get(2)), userAnswers);
    }
}