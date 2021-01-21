package org.course.service.intefaces;

public interface BookCommentHandleService {

    String createBookComment(long bookId, long userId, String text);

    String readBookComment(long id);

    String readAllBookComments(int page, int size);

    String updateBookComment(long id, String text);

    String deleteBookComment(long id);

    String deleteAllBookComments();

    String countBookComments();
}
