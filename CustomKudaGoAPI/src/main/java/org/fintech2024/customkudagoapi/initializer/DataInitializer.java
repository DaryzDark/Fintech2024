package org.fintech2024.customkudagoapi.initializer;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.fintech2024.customkudagoapi.entity.Event;
import org.fintech2024.customkudagoapi.entity.Place;
import org.fintech2024.customkudagoapi.repository.PlaceRepository;
import org.fintech2024.customkudagoapi.service.EventService;
import org.fintech2024.logexecutiontimestarter.config.annotation.LogExecutionTime;
import org.fintech2024.customkudagoapi.model.Category;
import org.fintech2024.customkudagoapi.model.Location;
import org.fintech2024.customkudagoapi.repository.InMemoryGenericDataStore;
import org.fintech2024.customkudagoapi.service.FetchKudaGoAPIService;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class DataInitializer {

    private final FetchKudaGoAPIService apiService;
    private final InMemoryGenericDataStore<Category> categoryDataStore;
    private final InMemoryGenericDataStore<Location> locationDataStore;
    private final PlaceRepository placeRepository;
    private final EventService eventService;

    public DataInitializer(FetchKudaGoAPIService apiService,
                           InMemoryGenericDataStore<Category> categoryDataStore,
                           InMemoryGenericDataStore<Location> locationDataStore,
                           PlaceRepository placeRepository,
                           EventService eventService) {
        this.apiService = apiService;
        this.categoryDataStore = categoryDataStore;
        this.locationDataStore = locationDataStore;
        this.placeRepository = placeRepository;
        this.eventService = eventService;
    }

    @PostConstruct
    @LogExecutionTime
    public void initData() {
        log.info("Data initialization started.");

        try {
            log.info("Request categories from the KudaGo API...");
            List<Category> categories = apiService.fetchCategories();
            categories.forEach(categoryDataStore::add);
            log.info("The categories have been successfully uploaded and saved. Total categories: {}", categories.size());
        } catch (Exception e) {
            log.error("Categories load error: {}", e.getMessage());
        }

        try {
                log.info("Requesting locations from the KudaGo API...");
            List<Location> locations = apiService.fetchLocations();
            locations.forEach(locationDataStore::add);
            log.info("The locations have been successfully uploaded and saved. Total locations:{}", locations.size());
        } catch (Exception e) {
            log.error("Location load error: {}", e.getMessage());
        }
        try {
            log.info("Requesting places from the KudaGo API...");
            List<Place> places = apiService.fetchPlaces().getResults();
            placeRepository.saveAll(places);
            log.info("The places have been successfully uploaded and saved. Total places:{}", places.size());
        } catch (Exception e) {
            log.error("Places load error: {}", e.getMessage());
        }

        try {
            log.info("Requesting events from the KudaGo API...");
            List<Event> events = apiService.fetchEvents().getResults();
            eventService.saveAllEvents(events);
            log.info("The events have been successfully uploaded and saved. Total events:{}", events.size());
        } catch (Exception e) {
            log.error("Events load error: {}", e.getMessage());
        }

        log.info("Data initialization finished.");
    }
}
