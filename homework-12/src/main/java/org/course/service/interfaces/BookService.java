package org.course.service.interfaces;

import org.course.api.request.BookRequest;

public interface BookService {

    void deleteBook(long id);

    void updateBook(long id, BookRequest request);

    void createBook(BookRequest request);

}
