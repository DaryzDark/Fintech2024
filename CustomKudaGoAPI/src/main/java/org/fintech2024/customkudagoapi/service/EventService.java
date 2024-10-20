package org.fintech2024.customkudagoapi.service;

import org.fintech2024.customkudagoapi.model.Event;
import org.fintech2024.customkudagoapi.model.EventResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class EventService {

    private final WebClient webClient;
    private final CurrencyConversionService currencyConversionService;

    public EventService(CurrencyConversionService currencyConversionService, WebClient.Builder webClientBuilder) {
        this.currencyConversionService = currencyConversionService;
        this.webClient = webClientBuilder.baseUrl("https://kudago.com").build(); // Инициализируем WebClient
    }

    public CompletableFuture<List<Event>> getPopularEvents(LocalDate dateFrom, LocalDate dateTo, BigDecimal budget, String currency) {
        CompletableFuture<List<Event>> eventsFuture = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/public-api/v1.4/events/")
                        .queryParam("actual_since", dateFrom.toEpochDay())
                        .queryParam("actual_until", dateTo.toEpochDay())
                        .queryParam("fields", "id,title,description,price,site_url")
                        .build())
                .retrieve()
                .bodyToMono(EventResponse.class)
                .map(EventResponse::getResults)
                .toFuture();

        CompletableFuture<BigDecimal> budgetFuture = currencyConversionService.convertToRubles(budget, currency);

        CompletableFuture<List<Event>> resultFuture = new CompletableFuture<>();

        eventsFuture.thenAcceptBoth(budgetFuture, (events, rubBudget) -> {
            List<Event> filteredEvents = events.stream()
                    .filter(event -> event.getPrice() != null && event.getPrice().compareTo(rubBudget) <= 0)
                    .collect(Collectors.toList());

            resultFuture.complete(filteredEvents);
        }).exceptionally(ex -> {
            resultFuture.completeExceptionally(ex);
            return null;
        });

        return resultFuture;
    }
}
