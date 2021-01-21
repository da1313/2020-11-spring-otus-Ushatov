package org.course.service.intefaces;

public interface AuthorHandlerService {

    String createAuthor(String name);

    String readAuthor(long id);

    String readAllAuthors();

    String updateAuthor(long id, String name);

    String deleteAuthor(long id);

    String deleteAllAuthors();

    String countAuthors();

}
