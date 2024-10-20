package org.fintech2024.customkudagoapi.controller;

import org.fintech2024.customkudagoapi.model.Event;
import org.fintech2024.customkudagoapi.service.EventService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public CompletableFuture<ResponseEntity<List<Event>>> getEvents(
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

        return eventService.getPopularEvents(dateFrom, dateTo, budget, currency)
                .thenApply(filteredEvents -> {
                    if (filteredEvents.isEmpty()) {
                        return ResponseEntity.noContent().build();
                    } else {
                        return ResponseEntity.ok(filteredEvents);
                    }
                });
    }
}
