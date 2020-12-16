package org.course.homework.service.interfaces;

import java.util.List;

public interface NumberParser {
    List<Integer> parseAnswerNumbers(int size, String userAnswers);
}
