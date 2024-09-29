package org.fintech2024.logexecutiontimestarter.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class LogExecutionTimeAutoConfiguration {


    @Bean
    public static PostConstructLogExecutionTimeAnnotationBeanPostProcessor logExecutionTimePostProcessor() {
        return new PostConstructLogExecutionTimeAnnotationBeanPostProcessor();
    }
}
