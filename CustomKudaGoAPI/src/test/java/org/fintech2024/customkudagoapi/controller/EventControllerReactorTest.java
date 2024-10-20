package org.fintech2024.customkudagoapi.controller;

import org.fintech2024.customkudagoapi.service.EventServiceReactor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.fintech2024.customkudagoapi.model.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class EventControllerReactorTest {
    @Mock
    private EventServiceReactor eventServiceReactor;

    @InjectMocks
    private EventControllerReactor eventControllerReactor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetEventsWithResults() {
        LocalDate dateFrom = LocalDate.of(2024, 10, 1);
        LocalDate dateTo = LocalDate.of(2024, 10, 7);
        BigDecimal budget = BigDecimal.valueOf(1000);
        String currency = "USD";

        Event event = new Event();
        event.setTitle("Sample Event");
        List<Event> eventList = Collections.singletonList(event);

        when(eventServiceReactor.getPopularEvents(dateFrom, dateTo, budget, currency))
                .thenReturn(Mono.just(eventList));

        Mono<ResponseEntity<List<Event>>> response = eventControllerReactor.getEvents(budget, currency, dateFrom, dateTo);

        StepVerifier.create(response)
                .assertNext(res -> {
                    assertThat(res.getStatusCodeValue()).isEqualTo(200);
                    assertThat(res.getBody()).hasSize(1);
                    assertThat(res.getBody().get(0).getTitle()).isEqualTo("Sample Event");
                })
                .verifyComplete();
    }

    @Test
    void testGetEventsWithNoResults() {
        LocalDate dateFrom = LocalDate.of(2024, 10, 1);
        LocalDate dateTo = LocalDate.of(2024, 10, 7);
        BigDecimal budget = BigDecimal.valueOf(1000);
        String currency = "USD";

        when(eventServiceReactor.getPopularEvents(dateFrom, dateTo, budget, currency))
                .thenReturn(Mono.just(Collections.emptyList()));

        Mono<ResponseEntity<List<Event>>> response = eventControllerReactor.getEvents(budget, currency, dateFrom, dateTo);

        StepVerifier.create(response)
                .assertNext(res -> {
                    assertThat(res.getStatusCodeValue()).isEqualTo(204);
                    assertThat(res.getBody()).isNull();
                })
                .verifyComplete();
    }

    @Test
    void testGetEventsWithDefaultDateRange() {
        BigDecimal budget = BigDecimal.valueOf(1000);
        String currency = "USD";
        LocalDate now = LocalDate.now();

        List<Event> eventList = Collections.singletonList(new Event());
        when(eventServiceReactor.getPopularEvents(
                now.with(DayOfWeek.MONDAY),
                now.with(DayOfWeek.SUNDAY),
                budget,
                currency))
                .thenReturn(Mono.just(eventList));

        Mono<ResponseEntity<List<Event>>> response = eventControllerReactor.getEvents(budget, currency, null, null);

        StepVerifier.create(response)
                .assertNext(res -> {
                    assertThat(res.getStatusCodeValue()).isEqualTo(200);
                    assertThat(res.getBody()).isNotNull();
                })
                .verifyComplete();
    }

}