package com.horrorcore.utils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Aspect
@Component
public class LoggingAspect {

    private final Logger LOGGER = Logger.getLogger(LoggingAspect.class.getName());

    @Around("execution(* com.horrorcore.services.*.*(..))")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object results = joinPoint.proceed();
        long finishTime = System.currentTimeMillis() - startTime;
        LOGGER.info("Method " + joinPoint.getSignature() + " executed in " + finishTime + " ms");
        return results;
    }
}
