package org.course.homework.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Data
public class Question {
    private final String content;
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private final List<Answer> answers;
}
