package org.fintech2024.customkudagoapi.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LogAspect {

    @Around("@within(org.fintech2024.customkudagoapi.annotation.LogExecutionTime) || " +
            "@annotation(org.fintech2024.customkudagoapi.annotation.LogExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        Object proceed = joinPoint.proceed();

        long executionTime = System.currentTimeMillis() - start;

        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();

        log.info("Method execution {}.{} took {} ms", className, methodName, executionTime);

        return proceed;
    }
}
