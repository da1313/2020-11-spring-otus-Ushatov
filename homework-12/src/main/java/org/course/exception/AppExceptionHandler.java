package org.course.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    private ModelAndView handleEntityNotFoundException(EntityNotFoundException exception){
        return new ModelAndView("error");
    }

}
