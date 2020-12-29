package org.course.service.intefaces;

public interface BookHandlerService {

    String createBook(String name, String authorId, String genreId);

    String readBook(long id);

    String readAllBooks();

    String updateBook(long id, String name, long authorId, String authorName, long genreId, String genreName);

    String deleteBook(long id);

    String deleteAllBooks();

    String countBooks();

    String addCategory(long bookId, long categoryId);

    String removeCategory(long bookId, long categoryId);

}
