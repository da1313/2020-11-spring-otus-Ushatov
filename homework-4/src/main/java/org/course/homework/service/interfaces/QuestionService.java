package org.course.homework.service.interfaces;

import org.course.homework.domain.Question;

import java.util.List;

public interface QuestionService {
    List<Question> getQuestions(int questionCount);
}
