package org.course.homework.dao;

import lombok.AllArgsConstructor;
import org.course.homework.config.TestProperties;
import org.course.homework.domain.Question;
import org.course.homework.service.interfaces.CsvParser;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
@AllArgsConstructor
public class QuestionDaoLocale implements QuestionDao {

    private final TestProperties testProperties;
    private final MessageSource messageSource;
    private final CsvParser parser;

    @Override
    public List<Question> getAllQuestions() {
        String csvResource = String.format("%s%s%s_%s.properties",
                testProperties.getLocaleLocation(),
                testProperties.getLocaleLocation().equals("") || testProperties.getLocaleLocation().endsWith("/")? "" : "/",
                testProperties.getLocaleNamePrefix(),
                testProperties.getLocale());
        try(InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(csvResource)){
            if (inputStream == null){
                throw new IllegalArgumentException("File not found! " + csvResource);
            }
            List<String> csvLines = Arrays.asList(new String(inputStream.readAllBytes(), StandardCharsets.UTF_8).split("\\v+"));
            return csvLines.stream()
                    .filter(l -> !l.equals(""))
                    .map(this::process)
                    .filter(Objects::nonNull)
                    .map(l -> messageSource.getMessage(l, new String[]{}, testProperties.getLocale()))
                    .map(parser::parse).collect(Collectors.toList());

        } catch (IOException e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private String process(String line){
        Matcher matcher = Pattern.compile("question.\\d+").matcher(line);
        if (matcher.find()){
            return line.substring(matcher.start(), matcher.end());
        } else {
            return null;
        }
    }
}
