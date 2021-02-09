package org.course.service.intefaces;

public interface AuthorHandlerService {

    String createAuthor(String name);

    String readAuthor(String id);

    String readAllAuthors();

    String updateAuthor(String id, String name);

    String deleteAuthor(String id);

    String deleteAllAuthors();

    String countAuthors();

}
