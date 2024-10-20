package org.fintech2024.customkudagoapi.controller;

import org.fintech2024.customkudagoapi.model.Event;
import org.fintech2024.customkudagoapi.service.EventServiceReactor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/eventsreactor")
public class EventControllerReactor {
    private final EventServiceReactor eventServiceReactor;

    public EventControllerReactor(EventServiceReactor eventService) {
        this.eventServiceReactor = eventService;
    }

    @GetMapping
    public Mono<ResponseEntity<List<Event>>> getEvents(
            @RequestParam("budget") BigDecimal budget,
            @RequestParam("currency") String currency,
            @RequestParam(value = "dateFrom", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            @RequestParam(value = "dateTo", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo) {

        LocalDate now = LocalDate.now();
        if (dateFrom == null) {
            dateFrom = now.with(DayOfWeek.MONDAY);
        }
        if (dateTo == null) {
            dateTo = now.with(DayOfWeek.SUNDAY);
        }

        return eventServiceReactor.getPopularEvents(dateFrom, dateTo, budget, currency)
                .flatMap(filteredEvents -> {
                    if (filteredEvents.isEmpty()) {
                        return Mono.just(ResponseEntity.noContent().build());
                    } else {
                        return Mono.just(ResponseEntity.ok(filteredEvents));
                    }
                });
    }
}
