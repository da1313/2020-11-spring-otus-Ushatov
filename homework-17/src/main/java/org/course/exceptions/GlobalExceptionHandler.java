package org.course.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> entityNotFoundExceptionHandler(EntityNotFoundException e){
        Map<String, Object> response = new HashMap<>();
        response.put("error", e.getMessage());
        return ResponseEntity.ok(response);
    }

}
