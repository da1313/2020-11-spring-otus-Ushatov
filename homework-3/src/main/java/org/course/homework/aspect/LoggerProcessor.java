package org.course.homework.aspect;

import lombok.SneakyThrows;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.course.homework.domain.Question;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggerProcessor{

    private final Logger logger = LoggerFactory.getLogger(LoggerProcessor.class);

    @SneakyThrows
    @Around("@annotation(Log)")
    public Object log(ProceedingJoinPoint joinPoint){
        if (joinPoint.getSignature().getName().equals("readUserName")){
            Object username = joinPoint.proceed();
            logger.trace("User " + username + " start test!");
            return username;
        }
        if (joinPoint.getSignature().getName().equals("readUserAnswers")){
            Object answers = joinPoint.proceed();
            logger.trace("User select: " + answers);
            return answers;
        }
        if (joinPoint.getSignature().getName().equals("printQuestion")){
            Object answers = joinPoint.proceed();
            logger.trace("Next question is: " + ((Question) joinPoint.getArgs()[0]).getQuestionContent());
            return answers;
        }
        if (joinPoint.getSignature().getName().equals("printResult")){
            logger.trace("User " + joinPoint.getArgs()[0] + " rate: " + joinPoint.getArgs()[1] + " current to pass " + joinPoint.getArgs()[2]);
        }
        return joinPoint.proceed();
    }
}
