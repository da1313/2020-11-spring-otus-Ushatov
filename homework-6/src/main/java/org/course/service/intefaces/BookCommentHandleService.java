package org.course.service.intefaces;

public interface BookCommentHandleService {

    String createBookComment(long bookId, String text);

    String readBookComment(long id);

    String readAllBookComments();

    String updateBookComment(long id, String text);

    String deleteBookComment(long id);

    String deleteAllBookComments();

    String countBookComments();
}
