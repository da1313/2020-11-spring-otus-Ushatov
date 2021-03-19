package org.course.exceptions;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler
    private ModelAndView handleEntityNotFoundException(RuntimeException exception){
        return new ModelAndView("error");
    }

}
