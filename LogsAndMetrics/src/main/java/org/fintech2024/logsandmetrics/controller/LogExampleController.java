package org.fintech2024.logsandmetrics.controller;

import lombok.extern.slf4j.Slf4j;
import org.fintech2024.logsandmetrics.metric.CustomMetric;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api")
@Slf4j
public class LogExampleController {

    private final CustomMetric customMetric;

    public LogExampleController( CustomMetric customMetric) {
        this.customMetric = customMetric;
    }

    @GetMapping("/log")
    public String logMessage() {
        var userID = UUID.randomUUID().toString();
        MDC.put(userID,"Example message");
        log.info("Example log message with structured data.");
        MDC.clear();
        return "Log message sent!";
    }

    @GetMapping("/increment")
    public String incrementMetric() {
        customMetric.increment();
        return "Custom metric incremented!";
    }
}
