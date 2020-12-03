package ru.otus.homework.service;

import org.course.homework.domain.Answer;
import org.course.homework.domain.Question;
import org.course.homework.service.TestServiceImpl;
import org.course.homework.service.interfaces.QuestionService;
import org.course.homework.service.interfaces.TestAdvisorService;
import org.course.homework.service.interfaces.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Class TestServiceImpl")
@ExtendWith(MockitoExtension.class)
class TestServiceImplTest {

    @Mock
    private QuestionService questionService;
    @Mock
    private UserService userService;
    @Mock
    private TestAdvisorService testAdvisorService;
    private List<Question> questionList;

    @BeforeEach
    void questionInit(){
        questionList = new ArrayList<>();
        List<Answer> answerList = new ArrayList<>();
        Question question = new Question("Question 1", answerList);
        answerList.add(new Answer("Answer 11", question, true));
        answerList.add(new Answer("Answer 12", question, false));
        answerList.add(new Answer("Answer 13", question, true));
        questionList.add(question);
        List<Answer> answerList1 = new ArrayList<>();
        Question question1 = new Question("Question 2", answerList1);
        answerList1.add(new Answer("Answer 21", question, true));
        answerList1.add(new Answer("Answer 22", question, false));
        answerList1.add(new Answer("Answer 23", question, false));
        questionList.add(question1);
        List<Answer> answerList2 = new ArrayList<>();
        Question question2 = new Question("Question 3", answerList2);
        answerList2.add(new Answer("Answer 21", question, false));
        answerList2.add(new Answer("Answer 22", question, true));
        answerList2.add(new Answer("Answer 23", question, true));
        questionList.add(question2);
    }

    @DisplayName("Ответы считаются верно")
    @Test
    void run() {
        Mockito.lenient().when(questionService.getQuestions()).thenReturn(questionList);
        Mockito.lenient().when(userService.readUserName()).thenReturn("Ivan Ivanov");
        //first question fail
        Mockito.lenient().when(userService.readAnswer(questionList.get(0).getAnswers()))
                .thenReturn(List.of(questionList.get(0).getAnswers().get(1)));
        //second question true
        Mockito.lenient().when(userService.readAnswer(questionList.get(1).getAnswers()))
                .thenReturn(List.of(questionList.get(1).getAnswers().get(0)));
        //third question fail
        Mockito.lenient().when(userService.readAnswer(questionList.get(2).getAnswers()))
                .thenReturn(List.of(questionList.get(2).getAnswers().get(0)));

        ArgumentCaptor<Integer> userAnswers = ArgumentCaptor.forClass(int.class);
        ArgumentCaptor<Integer> questionCount = ArgumentCaptor.forClass(int.class);
        ArgumentCaptor<String> userName = ArgumentCaptor.forClass(String.class);
        Mockito.doNothing().when(testAdvisorService).printResult(userName.capture(), userAnswers.capture(), questionCount.capture());

        //first question fail
        Mockito.when(testAdvisorService.validate(questionList.get(0).getAnswers(), List.of(questionList.get(0).getAnswers().get(1)))).thenReturn(false);
        //second question true
        Mockito.when(testAdvisorService.validate(questionList.get(1).getAnswers(), List.of(questionList.get(1).getAnswers().get(0)))).thenReturn(true);
        //third question fail
        Mockito.when(testAdvisorService.validate(questionList.get(2).getAnswers(), List.of(questionList.get(2).getAnswers().get(0)))).thenReturn(false);

        TestServiceImpl testService = new TestServiceImpl(questionService, userService, testAdvisorService);
        testService.run();

        assertAll(
                () -> assertEquals("Ivan Ivanov", userName.getValue()),
                () -> assertEquals(questionList.size(), questionCount.getValue()),
                () -> assertEquals(1, userAnswers.getValue())
        );
    }
}