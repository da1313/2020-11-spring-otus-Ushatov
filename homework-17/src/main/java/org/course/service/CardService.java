package org.course.service;

import org.course.dto.GenericResponse;

public interface CardService {

    GenericResponse takeBook(String bookId);

    GenericResponse returnBook(String bookId);

}
