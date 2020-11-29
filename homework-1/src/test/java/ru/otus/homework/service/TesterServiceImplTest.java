package ru.otus.homework.service;

import org.course.homework.service.RandomGeneratorImpl;
import org.course.homework.service.TesterServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.course.homework.domain.Answer;
import org.course.homework.domain.Question;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Класс TesterServiceImpl")
@ExtendWith(MockitoExtension.class)
class TesterServiceImplTest {

    @Mock
    private RandomGeneratorImpl randomGenerator;

    @DisplayName("Вывод правильный")
    @Test
    void outputTest() {
        List<Question> questionList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            List<Answer> answers = new ArrayList<>();
            Question question = new Question("q" + i, answers);
            for (int j = 0; j < 3; j++) {
                answers.add(new Answer("a" + i + j, question, true));
            }
            questionList.add(question);
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < questionList.size(); i++) {
            sb.append(i + 1).append(" ").append(questionList.get(i).getContent()).append("\n");
            for (int j = 0; j < questionList.get(i).getAnswers().size(); j++) {
                sb.append("\t").append(j + 1).append(" ").append(questionList.get(i).getAnswers().get(j).getContent()).append("\n");
            }
        }
        Mockito.lenient().when(randomGenerator.generate(5)).thenReturn(questionList);
        TesterServiceImpl testerService = new TesterServiceImpl(randomGenerator, 5);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(bos);
        PrintStream tmp = System.out;
        System.setOut(out);
        testerService.run();
        System.setOut(tmp);
        assertEquals(sb.toString(), bos.toString());
    }
}