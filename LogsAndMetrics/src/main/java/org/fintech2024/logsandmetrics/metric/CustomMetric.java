package org.fintech2024.logsandmetrics.metric;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class CustomMetric {

        private final Counter requestCounter;

        public CustomMetric(MeterRegistry meterRegistry) {
            this.requestCounter = meterRegistry.counter("custom_metric", "type", "example");
        }

        public void increment() {
            requestCounter.increment();
        }
}
