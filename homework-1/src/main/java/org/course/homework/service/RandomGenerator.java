package org.course.homework.service;

import org.course.homework.domain.Question;

import java.util.List;

public interface RandomGenerator {
    List<Question> generate(int count);
}
