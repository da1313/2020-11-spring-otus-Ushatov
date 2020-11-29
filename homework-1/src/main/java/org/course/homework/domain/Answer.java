package org.course.homework.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
public class Answer {
    private final String content;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private final Question question;
    @EqualsAndHashCode.Exclude
    private final boolean isCorrect;
}
