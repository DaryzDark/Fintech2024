package org.fintech2024.customkudagoapi.initializer;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.fintech2024.customkudagoapi.entity.Event;
import org.fintech2024.customkudagoapi.entity.Place;
import org.fintech2024.customkudagoapi.pattern.command.Command;
import org.fintech2024.customkudagoapi.pattern.command.LoadCategoriesCommand;
import org.fintech2024.customkudagoapi.pattern.command.LoadLocationsCommand;
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
    private final Command loadCategoriesCommand;
    private final Command loadLocationsCommand;
    private final PlaceRepository placeRepository;
    private final EventService eventService;

    public DataInitializer(FetchKudaGoAPIService apiService,
                           InMemoryGenericDataStore<Category> categoryDataStore,
                           InMemoryGenericDataStore<Location> locationDataStore,
                           PlaceRepository placeRepository,
                           EventService eventService) {
        this.apiService = apiService;
        this.loadLocationsCommand = new LoadLocationsCommand(apiService, locationDataStore);
        this.loadCategoriesCommand = new LoadCategoriesCommand(apiService, categoryDataStore);
        this.placeRepository = placeRepository;
        this.eventService = eventService;
    }

    @PostConstruct
    @LogExecutionTime
    public void initData() {
        log.info("Data initialization started.");

        loadCategoriesCommand.execute();
        loadLocationsCommand.execute();

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
