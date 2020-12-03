package org.course.homework.dao;

import org.course.homework.domain.Question;
import org.course.homework.service.interfaces.CsvParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class QuestionDaoCsv implements QuestionDao {

    private final String csvResource;
    private final CsvParser parser;

    public QuestionDaoCsv(@Value("${csvResourceLocation}") String csvResource, CsvParser parser) {
        this.csvResource = csvResource;
        this.parser = parser;
    }

    @Override
    public List<Question> getAllQuestions() {
        try(InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(csvResource)) {
            if (resourceAsStream == null){
                throw new IllegalArgumentException("File not found! " + csvResource);
            }
            List<String> csvLines = Arrays.asList(new String(resourceAsStream.readAllBytes()).split("[\n\r]"));
            return csvLines.stream()
                    .filter(l -> !l.equals(""))
                    .map(parser::parse).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
