package org.course.exception;

public class IllegalCommentAttemptException extends RuntimeException {
    public IllegalCommentAttemptException(String message) {
        super(message);
    }
}

