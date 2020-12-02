package org.course.homework.service;

import org.course.homework.dao.QuestionDao;
import org.course.homework.domain.Question;

import java.util.*;

/**
 * Предпологается что весе вопросы уникальные
 */
public class RandomGeneratorImpl implements RandomGenerator {
    private final QuestionDao questionDao;

    public RandomGeneratorImpl(QuestionDao questionDao) {
        this.questionDao = questionDao;
    }

    @Override
    public List<Question> generate(int count) {
        List<Question> allQuestions = questionDao.getAllQuestions();
        Set<Question> result = new HashSet<>();
        if (allQuestions.size() < count){
            //throw new IllegalArgumentException("В базе нехватает вопросов!");
            count = allQuestions.size();
        }
        while (result.size() < count){
            result.add(allQuestions.get(new Random().nextInt(allQuestions.size())));
        }
        return new ArrayList<>(result);
    }
}
