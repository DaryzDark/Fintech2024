package org.fintech2024.customkudagoapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
@EnableScheduling
public class ExecutorConfig {

    @Value("${custom.threadpool.size:2}")
    private int threadPoolSize;

    @Bean(name = "dataInitializerExecutor")
    public ExecutorService dataInitializerExecutor() {
        return Executors.newFixedThreadPool(threadPoolSize, new CustomThreadFactory("data-init-pool"));
    }

    @Bean(name = "scheduledExecutorService")
    public ScheduledExecutorService scheduledExecutorService() {
        return Executors.newScheduledThreadPool(1, new CustomThreadFactory("scheduled-init-pool"));
    }

    static class CustomThreadFactory implements ThreadFactory {
        private final String namePrefix;
        private final AtomicInteger threadNumber = new AtomicInteger(1);

        CustomThreadFactory(String namePrefix) {
            this.namePrefix = namePrefix;
        }

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, namePrefix + "-thread-" + threadNumber.getAndIncrement());
        }
    }
}
