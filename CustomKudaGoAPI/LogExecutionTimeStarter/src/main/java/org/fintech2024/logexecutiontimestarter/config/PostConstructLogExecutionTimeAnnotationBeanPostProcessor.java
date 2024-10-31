package org.fintech2024.logexecutiontimestarter.config;


import lombok.extern.slf4j.Slf4j;
import org.fintech2024.logexecutiontimestarter.config.annotation.LogExecutionTime;
import org.fintech2024.logexecutiontimestarter.config.interceptor.LogExecutionTimeInterceptor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
@Slf4j
public class PostConstructLogExecutionTimeAnnotationBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        boolean isClassAnnotated = bean.getClass().isAnnotationPresent(LogExecutionTime.class);
        boolean isMethodAnnotated = false;
        for (Method method : bean.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(LogExecutionTime.class)) {
                isMethodAnnotated = true;
                break;
            }
        }
        if (isClassAnnotated || isMethodAnnotated) {
            ProxyFactory proxyFactory = new ProxyFactory(bean);
            proxyFactory.addAdvice(new LogExecutionTimeInterceptor());
            return proxyFactory.getProxy();
        } else {
            return bean;
        }
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

}
