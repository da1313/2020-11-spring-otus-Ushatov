package org.course.homework.service;

import org.course.homework.dao.QuestionDaoCsv;
import org.course.homework.domain.Answer;
import org.course.homework.domain.Question;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Класс RandomGeneratorImpl")
class RandomGeneratorImplTest {

    @Mock
    private QuestionDaoCsv questionDaoCsv;

    private List<Question> questionList;

    @BeforeEach
    void init(){
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

    @DisplayName("Количество сгенерированных вопросов совпвдвет с заданным")
    @Test
    void getGeneratedCount() {
        RandomGeneratorImpl generator = new RandomGeneratorImpl(questionDaoCsv);
        Mockito.lenient().when(questionDaoCsv.getAllQuestions()).thenReturn(questionList);
        List<Question> result = generator.generate(3);
        Assertions.assertEquals(3, result.size());
    }

    @DisplayName("Количество сгенерированных вопросов урезается до максимального")
    @Test
    void getGeneratedCountCapped() {
        RandomGeneratorImpl generator = new RandomGeneratorImpl(questionDaoCsv);
        Mockito.lenient().when(questionDaoCsv.getAllQuestions()).thenReturn(questionList);
        List<Question> result = generator.generate(100);
        Assertions.assertEquals(3, result.size());
    }

    @DisplayName("Нет повторяющихся вопросов")
    @Test
    void checkRepeats() {
        RandomGeneratorImpl generator = new RandomGeneratorImpl(questionDaoCsv);
        Mockito.lenient().when(questionDaoCsv.getAllQuestions()).thenReturn(questionList);
        List<Question> result = generator.generate(3);
        org.assertj.core.api.Assertions.assertThat(result).contains(questionList.get(0), questionList.get(1), questionList.get(2));
    }

    @DisplayName("Вопросы перемешиваются")
    @Test
    void checkRandom() {
        List<Question> questionList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Question question = new Question("q" + i, new ArrayList<>());
            questionList.add(question);
        }
        RandomGeneratorImpl generator = new RandomGeneratorImpl(questionDaoCsv);
        Mockito.lenient().when(questionDaoCsv.getAllQuestions()).thenReturn(questionList);
        List<Question> result = generator.generate(100);
        org.assertj.core.api.Assertions.assertThat(result).isNotEqualTo(questionList);
    }
}