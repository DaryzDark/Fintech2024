package org.fintech2024.customkudagoapi.service;

import org.fintech2024.customkudagoapi.model.Event;
import org.fintech2024.customkudagoapi.model.EventResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class EventServiceReactor {
    private final WebClient webClient;
    private final CurrencyConversionServiceReactor currencyConversionServiceReactor;

    public EventServiceReactor(CurrencyConversionServiceReactor currencyConversionServiceReactor, WebClient.Builder webClientBuilder) {
        this.currencyConversionServiceReactor = currencyConversionServiceReactor;
        this.webClient = webClientBuilder.baseUrl("https://kudago.com").build();
    }

    public Mono<List<Event>> getPopularEvents(LocalDate dateFrom, LocalDate dateTo, BigDecimal budget, String currency) {
        String params = "&fields=id,title,description,price,site_url";
        String url = String.format("/public-api/v1.4/events/?actual_since=%s&actual_until=%s" + params,
                dateFrom.toEpochDay(), dateTo.toEpochDay());

        Mono<List<Event>> eventsMono = webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(EventResponse.class)
                .map(EventResponse::getResults);

        Mono<BigDecimal> budgetMono = currencyConversionServiceReactor.convertToRubles(budget, currency);

        return Mono.zip(eventsMono, budgetMono)
                .flatMap(tuple -> {
                    List<Event> events = tuple.getT1();
                    BigDecimal rubBudget = tuple.getT2();

                    List<Event> filteredEvents = events.stream()
                            .filter(event -> event.getPrice() != null && event.getPrice().compareTo(rubBudget) <= 0)
                            .toList();

                    return Mono.just(filteredEvents);
                });
    }
}
