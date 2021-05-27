package org.course.exception;

import org.course.api.response.GenericResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<GenericResponse> handleIllegalCommentAttemptException(IllegalCommentAttemptException exception){
        return ResponseEntity.ok(new GenericResponse(false));
    }

}
