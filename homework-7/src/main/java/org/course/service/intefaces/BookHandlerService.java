package org.course.service.intefaces;

public interface BookHandlerService {

    String createBook(String name, String authorId, String genreId);

    String readBook(long id, int page, int size);

    String readAllBooks(int page, int size);

    String updateBook(long id, String name, long authorId, String authorName);

    String deleteBook(long id);

    String deleteAllBooks();

    String countBooks();

    String addGenre(long bookId, long genreId);

    String removeGenre(long bookId, long genreId);

}
