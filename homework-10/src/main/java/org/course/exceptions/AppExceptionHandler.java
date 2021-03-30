package org.course.exceptions;

import org.course.api.responces.ResultResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ResultResponse> handleEntityNotFoundException(EntityNotFoundException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResultResponse(false));
    }

}
