package org.course.homework.dao;

import org.course.homework.domain.Question;
import org.course.homework.util.CsvParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class QuestionDaoCsv implements QuestionDao {
    private String csvResource;

    @Override
    public List<Question> getAllQuestions() {
        try(InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(csvResource)) {
            if (resourceAsStream == null){
                throw new IllegalArgumentException("Файл не найден! " + csvResource);
            }
            List<String> csvLines = Arrays.asList(new String(resourceAsStream.readAllBytes()).split("[\n\r]"));
            return csvLines.stream()
                    .filter(l -> !l.equals(""))
                    .map(CsvParser::parse).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void setCsvResource(String csvResource) {
        this.csvResource = csvResource;
    }
}
