package org.course.homework.aspect;

import lombok.SneakyThrows;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggerProcessor{

    private final Logger logger = LoggerFactory.getLogger(LoggerProcessor.class);

    @SneakyThrows
    @Around("@annotation(LogUserInput)")
    public Object log(ProceedingJoinPoint joinPoint){
        Object result = joinPoint.proceed();
        logger.trace("User input: " + result);
        return result;
    }

}
