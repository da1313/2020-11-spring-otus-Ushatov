package org.course.homework.dao;

import lombok.AllArgsConstructor;
import org.course.homework.dao.interfaces.QuestionDao;
import org.course.homework.domain.Question;
import org.course.homework.service.interfaces.CsvParser;
import org.course.homework.service.interfaces.LocaleResourceLocator;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
public class QuestionDaoCsv implements QuestionDao {

    private final LocaleResourceLocator resourceLocator;
    private final CsvParser parser;

    @Override
    public List<Question> getAllQuestions() {
        String csvResource = resourceLocator.getResource();
        try(InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(csvResource)) {
            if (resourceAsStream == null){
                throw new IllegalArgumentException("File not found! " + csvResource);
            }
            List<String> csvLines = Arrays.asList(new String(resourceAsStream.readAllBytes(), StandardCharsets.UTF_8).split("\\v+"));
            return csvLines.stream()
                    .filter(l -> !l.equals(""))
                    .map(parser::parse).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
