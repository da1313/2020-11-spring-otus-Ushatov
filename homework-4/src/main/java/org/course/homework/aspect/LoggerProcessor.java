package org.course.homework.aspect;

import lombok.SneakyThrows;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Aspect
@Component
public class LoggerProcessor{

    private final Logger logger = LoggerFactory.getLogger(LoggerProcessor.class);

    @SneakyThrows
    @Around("@annotation(LogMethod)")
    public Object log(ProceedingJoinPoint joinPoint){
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        LogMethod annotation = method.getAnnotation(LogMethod.class);
        String argsPattern = annotation.argsPattern();
        String resultPattern = annotation.resultPattern();
        int[] ints = annotation.argsOrder();
        List<Object> args = new ArrayList<>();
        for (int i = 0; i < ints.length; i++) {
            int nextIndex = ints[i];
            if (nextIndex > ints.length || nextIndex < 0 ) continue;
            args.add(joinPoint.getArgs()[i]);
        }
        if (!argsPattern.isEmpty()){
            logger.trace(String.format(argsPattern, args.toArray()));
        }
        Object proceed = joinPoint.proceed();
        if (!resultPattern.isEmpty()){
            logger.trace(String.format(resultPattern, proceed.toString()));
        }
        return proceed;
    }
}
