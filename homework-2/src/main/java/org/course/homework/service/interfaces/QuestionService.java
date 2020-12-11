package org.course.homework.service.interfaces;

import org.course.homework.domain.Answer;
import org.course.homework.domain.Question;

import java.util.List;

public interface QuestionService {
    List<Question> getQuestions();
    void printQuestion(Question question, int number);
    void printAnswers(List<Answer> answers);
}
