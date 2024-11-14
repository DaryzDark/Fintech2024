package org.fintech2024.logexecutiontimestarter.config.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.fintech2024.logexecutiontimestarter.config.annotation.LogExecutionTime;

@Slf4j
public class LogExecutionTimeInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Class<?> methodclass = invocation.getMethod().getDeclaringClass();
        if (methodclass.isAnnotationPresent(LogExecutionTime.class)
                || invocation.getMethod().isAnnotationPresent(LogExecutionTime.class)) {String methodName = invocation.getMethod().getName();
            Long startTime = System.currentTimeMillis();
            Object result = invocation.proceed();
            Long endTime = System.currentTimeMillis();
            log.info("Method {} was executed in {} ms", methodName, endTime - startTime);
            return result;
        } else {
            return invocation.proceed();
        }

    }
}
