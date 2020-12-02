package ru.otus.homework.dao;

import org.course.homework.dao.QuestionDaoCsv;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.course.homework.domain.Answer;
import org.course.homework.domain.Question;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Класс QuestionDaoCsv")
class QuestionDaoCsvTest {

    void createTestFile(List<String> lines, String fileName){
        try{
            PrintWriter printWriter = new PrintWriter(Paths.get("target/classes/" + fileName).toFile());
            lines.forEach(printWriter::println);
            printWriter.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    void deleteTestFile(String fileName){
        try{
            Files.delete(Paths.get("target/classes/" + fileName));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @DisplayName("При неправильном имени файла кидается исключение")
    @Test
    void throwsExceptionsIfFileNotFound() {
        String fileName = "notExistingFile.csv";
        QuestionDaoCsv questionDaoCsv = new QuestionDaoCsv();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            questionDaoCsv.setCsvResource(fileName);
            questionDaoCsv.getAllQuestions();
        });
        assertEquals("Файл не найден! " + fileName, exception.getMessage());
    }

    @DisplayName("Количество вопросов в файле равно количеству считанных")
    @Test
    void getAllQuestionsCount(){
        List<String> testLines = new ArrayList<>();
        testLines.add("q1,a1,a2,a3");
        testLines.add("q2,a1,a2,a3");
        testLines.add("q3,a1,a2,a3");
        String fileName = "test.csv";
        createTestFile(testLines, fileName);

        QuestionDaoCsv questionDaoCsv = new QuestionDaoCsv();
        questionDaoCsv.setCsvResource("test.csv");
        List<Question> allQuestions = questionDaoCsv.getAllQuestions();
        assertEquals(testLines.size(), allQuestions.size());

        deleteTestFile(fileName);
    }

    @DisplayName("Содержание всех вопросов считанно верно")
    @Test
    void getAllQuestionsNames(){
        List<String> testLines = new ArrayList<>();
        testLines.add("question 1,a1,a2,a3");
        testLines.add("q2,a1,a2,a3");
        testLines.add("Q3 ,a1,a2,a3");
        String fileName = "test.csv";
        createTestFile(testLines, fileName);

        QuestionDaoCsv questionDaoCsv = new QuestionDaoCsv();
        questionDaoCsv.setCsvResource(fileName);
        List<Question> allQuestions = questionDaoCsv.getAllQuestions();
        assertEquals("question 1", allQuestions.get(0).getContent());
        assertEquals("q2", allQuestions.get(1).getContent());
        assertEquals("Q3 ", allQuestions.get(2).getContent());

        deleteTestFile(fileName);
    }

    @DisplayName("Содержание всех ответов верное")
    @Test
    void getAllQuestionsAnswersNames(){
        List<String> testLines = new ArrayList<>();
        testLines.add("question 1,a11,a12,a13");
        testLines.add("q2,a21,a22");
        String fileName = "test.csv";
        createTestFile(testLines, fileName);

        QuestionDaoCsv questionDaoCsv = new QuestionDaoCsv();
        questionDaoCsv.setCsvResource(fileName);
        List<Question> allQuestions = questionDaoCsv.getAllQuestions();
        assertLinesMatch(List.of("a11","a12","a13"), allQuestions.get(0).getAnswers().stream().map(Answer::getContent).collect(Collectors.toList()));
        assertLinesMatch(List.of("a21","a22"), allQuestions.get(1).getAnswers().stream().map(Answer::getContent).collect(Collectors.toList()));
        deleteTestFile(fileName);
    }
}