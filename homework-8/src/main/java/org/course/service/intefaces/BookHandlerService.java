package org.course.service.intefaces;

public interface BookHandlerService {

    String createBook(String name, String authorId, String genreId);

    String readBook(String id, int page, int size);

    String readAllBooks(int page, int size);

    String updateBook(String id, String name);

    String deleteBook(String id);

    String deleteAllBooks();

    String countBooks();

    String addGenre(String bookId, String genreId);

    String removeGenre(String bookId, String genreId);

}
