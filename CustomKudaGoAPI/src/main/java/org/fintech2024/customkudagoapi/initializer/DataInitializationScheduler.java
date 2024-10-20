package org.fintech2024.customkudagoapi.initializer;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class DataInitializationScheduler implements ApplicationListener<ApplicationStartedEvent> {
    private final ScheduledExecutorService scheduledExecutorService;
    private final DataInitializer dataInitializer;

    @Value("${custom.threadpool.size}")
    private int threadPoolSize;

    @Value("${custom.data.refresh.interval}")
    private  Duration scheduleInterval;

    public DataInitializationScheduler(
            @Qualifier("scheduledExecutorService") ScheduledExecutorService scheduledExecutorService,
            DataInitializer dataInitializer
    ) {
        this.scheduledExecutorService = scheduledExecutorService;
        this.dataInitializer = dataInitializer;
    }

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            try {
                log.info("Refreshing data...");
                log.info("Using {} threads", threadPoolSize);
                dataInitializer.initData();
            } catch (Exception e) {
                log.error("Error initializing data", e);
            }
        }, 0, scheduleInterval.toMillis(), TimeUnit.MILLISECONDS);
    }
}
