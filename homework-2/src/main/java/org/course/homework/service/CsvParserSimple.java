package org.course.homework.service;

import org.course.homework.domain.Answer;
import org.course.homework.domain.Question;
import org.course.homework.service.interfaces.CsvParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Предпологается что в тексте вопросов и ответов нет запятых
 */
@Service
public class CsvParserSimple implements CsvParser {

    private final int answerCount;

    public CsvParserSimple(@Value("${answersCount}") int answersCount) {
        this.answerCount = answersCount;
    }

    @Override
    public Question parse(String line){
        int splitCount = answerCount * 2 + 1;
        String[] tokens = line.split(",", splitCount);
        if (tokens.length < splitCount){
            throw new IllegalArgumentException("Parse error. Empty line or insufficient delimiters. " + line);
        }
        List<Answer> answers = new ArrayList<>();
        Question question = new Question(tokens[0], answers);
        for (int i = 1; i <= answerCount; i++) {
            if (tokens[i].equals("")) continue;
            if (tokens[i + answerCount].equals("")) continue;
            boolean isCorrect = tokens[i + answerCount].equals("1");
            answers.add(new Answer(tokens[i], question, isCorrect));
        }
        return new Question(tokens[0], answers);
    }
}
