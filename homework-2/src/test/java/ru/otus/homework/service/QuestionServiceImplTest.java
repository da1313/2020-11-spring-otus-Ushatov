package ru.otus.homework.service;

import org.course.homework.domain.Answer;
import org.course.homework.domain.Question;
import org.course.homework.service.*;
import org.course.homework.service.interfaces.PrintService;
import org.course.homework.service.interfaces.QuestionService;
import org.course.homework.service.interfaces.RandomGenerator;
import org.junit.jupiter.api.BeforeEach;
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

@DisplayName("Класс QuestionServiceImpl")
@ExtendWith(MockitoExtension.class)
class QuestionServiceImplTest {

    @Mock
    private RandomGenerator randomGenerator;
    private PrintService printService;
    private List<Question> questionList;
    public static final int QUESTION_COUNT = 3;
    public static final int QUESTION_NUMBER = 666;

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

    @DisplayName("Коллекция возвращенная генератором передается верно")
    @Test
    void getGeneratedCount() {
        Mockito.lenient().when(randomGenerator.generate(QUESTION_COUNT)).thenReturn(questionList);
        QuestionService questionService = new QuestionServiceImpl(randomGenerator, printService, QUESTION_COUNT);
        List<Question> questions = questionService.getQuestions();
        assertIterableEquals(questionList, questions);
    }

    @Test
    void printQuestion() {
        Mockito.lenient().when(randomGenerator.generate(QUESTION_COUNT)).thenReturn(questionList);
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        printService = new PrintServiceWithStream(new PrintStream(buffer), new ByteArrayInputStream("in".getBytes()));
        QuestionService questionService = new QuestionServiceImpl(randomGenerator, printService, QUESTION_COUNT);
        questionService.printQuestion(questionList.get(0), QUESTION_NUMBER);
        assertEquals("666 Question 1\n", new String(buffer.toByteArray()));
    }

    @Test
    void printAnswers() {
        Mockito.lenient().when(randomGenerator.generate(QUESTION_COUNT)).thenReturn(questionList);
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        printService = new PrintServiceWithStream(new PrintStream(buffer), new ByteArrayInputStream("in".getBytes()));
        QuestionService questionService = new QuestionServiceImpl(randomGenerator, printService, QUESTION_COUNT);
        questionService.printAnswers(questionList.get(0).getAnswers());
        assertEquals("\t1 Answer 11\n\t2 Answer 12\n\t3 Answer 13\n", new String(buffer.toByteArray()));
    }
}