package org.course.service.interfaces;

import org.course.api.requests.BookListRequest;
import org.course.api.requests.BookRequest;
import org.course.api.responces.BookInfoResponse;
import org.course.api.responces.BookListResponse;
import org.course.domain.Book;

public interface BookService {

    BookListResponse getBooks(BookListRequest request);

    BookListResponse getBooksByGenre(BookListRequest request);

    BookListResponse getBooksByQuery(BookListRequest request);

    BookInfoResponse getBookById(String id);

    Book createBook(BookRequest request);

    Book updateBook(String id, BookRequest request);

    void deleteBook(String id);
}
