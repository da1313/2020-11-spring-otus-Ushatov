package org.course.service.intefaces;

public interface CommentHandleService {

    String createBookComment(String bookId, String userId, String text);

    String readBookComment(String id);

    String readAllBookComments(int page, int size);

    String updateBookComment(String id, String text);

    String deleteBookComment(String id);

    String deleteAllBookComments();

    String countBookComments();
}
