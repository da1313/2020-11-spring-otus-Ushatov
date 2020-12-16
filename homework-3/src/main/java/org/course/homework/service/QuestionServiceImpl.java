package org.course.homework.service;

import lombok.AllArgsConstructor;
import org.course.homework.config.TestProperties;
import org.course.homework.domain.Question;
import org.course.homework.service.interfaces.QuestionService;
import org.course.homework.service.interfaces.RandomGenerator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final RandomGenerator randomGenerator;

    @Override
    public List<Question> getQuestions(int questionCount) {
        return randomGenerator.generate(questionCount);
    }
}
