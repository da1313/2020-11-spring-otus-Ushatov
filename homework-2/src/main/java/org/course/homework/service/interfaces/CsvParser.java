package org.course.homework.service.interfaces;

import org.course.homework.domain.Question;

public interface CsvParser {
    Question parse(String line);
}
