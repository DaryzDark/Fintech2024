package org.fintech2024.customkudagoapi.config;


import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import java.lang.reflect.Method;

@Component
@Slf4j
public class PostConstructLogExecutionTimeAnnotationBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Method[] methods = bean.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(PostConstruct.class)) {
                try {
                    long startTime = System.currentTimeMillis();
                    method.setAccessible(true);
                    method.invoke(bean);
                    long executionTime = System.currentTimeMillis() - startTime;
                    log.info("Method @PostConstruct {}.{} took {} ms", bean.getClass().getSimpleName(), method.getName(), executionTime);
                } catch (Exception e) {
                    log.error("Error invoking @PostConstruct method", e);
                }
            }
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
