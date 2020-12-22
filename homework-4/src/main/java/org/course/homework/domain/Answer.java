package org.course.homework.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
public class Answer {
    private final String answerContent;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private final Question question;
    private final boolean isCorrect;
}
