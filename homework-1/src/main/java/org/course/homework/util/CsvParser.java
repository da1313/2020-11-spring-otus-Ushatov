package org.course.homework.util;

import org.course.homework.domain.Answer;
import org.course.homework.domain.Question;

import java.util.ArrayList;
import java.util.List;

/**
 * Предпологается что в тексте вопросов и ответов нет запятых
 */
public final class CsvParser {
    public static Question parse(String line){
        String[] tokens = line.split(",");
        List<Answer> answers = new ArrayList<>();
        Question question = new Question(tokens[0], answers);
        if (tokens.length == 1){
            throw new IllegalArgumentException("Присуствует вопрос без ответа");
        }
        for (int i = 1; i < tokens.length; i++) {
            if (tokens[i].equals("")) continue;
            answers.add(new Answer(tokens[i], question,true));
        }
        return new Question(tokens[0], answers);
    }
}
