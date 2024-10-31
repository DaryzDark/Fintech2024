package org.fintech2024.customkudagoapi.pattern.command;

import lombok.extern.slf4j.Slf4j;
import org.fintech2024.customkudagoapi.model.Location;
import org.fintech2024.customkudagoapi.repository.InMemoryGenericDataStore;
import org.fintech2024.customkudagoapi.service.FetchKudaGoAPIService;

import java.util.List;

@Slf4j
public class LoadLocationsCommand implements Command {

    private final FetchKudaGoAPIService apiService;
    private final InMemoryGenericDataStore<Location> locationDataStore;

    public LoadLocationsCommand(FetchKudaGoAPIService apiService, InMemoryGenericDataStore<Location> locationDataStore) {
        this.apiService = apiService;
        this.locationDataStore = locationDataStore;
    }

    @Override
    public void execute() {
        try {
            log.info("Requesting locations from the KudaGo API...");
            List<Location> locations = apiService.fetchLocations();
            locations.forEach(locationDataStore::add);
            log.info("The locations have been successfully uploaded and saved. Total locations: {}", locations.size());
        } catch (Exception e) {
            log.error("Location load error: {}", e.getMessage());
        }
    }
}
