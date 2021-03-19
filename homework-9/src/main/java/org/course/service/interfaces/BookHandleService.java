package org.course.service.interfaces;

import org.course.dto.request.BookRequest;

public interface BookHandleService {

    void createBook(BookRequest request);

    void updateBook(long id, BookRequest request);

    void deleteBook(long id);
}
