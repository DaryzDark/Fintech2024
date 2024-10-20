package org.fintech2024.customkudagoapi.controller;

import org.fintech2024.customkudagoapi.model.Event;
import org.fintech2024.customkudagoapi.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class EventControllerTest {

    @Mock
    private EventService eventService;

    @InjectMocks
    private EventController eventController;

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

        when(eventService.getPopularEvents(dateFrom, dateTo, budget, currency))
                .thenReturn(CompletableFuture.completedFuture(eventList));

        CompletableFuture<ResponseEntity<List<Event>>> response = eventController.getEvents(budget, currency, dateFrom, dateTo);

        assertThat(response.join().getStatusCodeValue()).isEqualTo(200);
        assertThat(response.join().getBody()).hasSize(1);
        assertThat(response.join().getBody().get(0).getTitle()).isEqualTo("Sample Event");
    }

    @Test
    void testGetEventsWithNoResults() {
        // Настраиваем моки
        LocalDate dateFrom = LocalDate.of(2024, 10, 1);
        LocalDate dateTo = LocalDate.of(2024, 10, 7);
        BigDecimal budget = BigDecimal.valueOf(1000);
        String currency = "USD";

        when(eventService.getPopularEvents(dateFrom, dateTo, budget, currency))
                .thenReturn(CompletableFuture.completedFuture(Collections.emptyList()));

        CompletableFuture<ResponseEntity<List<Event>>> response = eventController.getEvents(budget, currency, dateFrom, dateTo);

        assertThat(response.join().getStatusCodeValue()).isEqualTo(204);
        assertThat(response.join().getBody()).isNull();
    }

    @Test
    void testGetEventsWithDefaultDateRange() {
        BigDecimal budget = BigDecimal.valueOf(1000);
        String currency = "USD";
        LocalDate now = LocalDate.now();

        List<Event> eventList = Collections.singletonList(new Event());
        when(eventService.getPopularEvents(
                now.with(DayOfWeek.MONDAY),
                now.with(DayOfWeek.SUNDAY),
                budget,
                currency))
                .thenReturn(CompletableFuture.completedFuture(eventList));

        CompletableFuture<ResponseEntity<List<Event>>> response = eventController.getEvents(budget, currency, null, null);

        assertThat(response.join().getStatusCodeValue()).isEqualTo(200);
        assertThat(response.join().getBody()).isNotNull();
    }
}