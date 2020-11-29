package org.course.homework.dao;


import org.course.homework.domain.Question;

import java.util.List;

public interface QuestionDao {
    List<Question> getAllQuestions();
}
