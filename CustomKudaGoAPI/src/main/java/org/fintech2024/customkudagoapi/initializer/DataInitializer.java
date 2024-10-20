package org.fintech2024.customkudagoapi.initializer;

import lombok.extern.slf4j.Slf4j;
import org.fintech2024.logexecutiontimestarter.config.annotation.LogExecutionTime;
import org.fintech2024.customkudagoapi.model.Category;
import org.fintech2024.customkudagoapi.model.Location;
import org.fintech2024.customkudagoapi.repository.InMemoryGenericDataStore;
import org.fintech2024.customkudagoapi.service.FetchKudaGoAPIService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;

@Slf4j
@Component
public class DataInitializer {

    private final FetchKudaGoAPIService apiService;
    private final InMemoryGenericDataStore<Category> categoryDataStore;
    private final InMemoryGenericDataStore<Location> locationDataStore;
    private final ExecutorService executorService;
    private final Semaphore semaphore;

    public DataInitializer(FetchKudaGoAPIService apiService,
                           InMemoryGenericDataStore<Category> categoryDataStore,
                           InMemoryGenericDataStore<Location> locationDataStore,
                           @Qualifier("dataInitializerExecutor") ExecutorService executorService,
                           @Value("${custom.max-concurrent-requests}") int maxConcurrentRequests) {
        this.apiService = apiService;
        this.categoryDataStore = categoryDataStore;
        this.locationDataStore = locationDataStore;
        this.executorService = executorService;
        this.semaphore = new Semaphore(maxConcurrentRequests);
    }

    @LogExecutionTime
    public void initData() {
        log.info("Data initialization started.");

        Future<?> categoryFuture = executorService.submit(this::loadCategories);
        Future<?> locationFuture = executorService.submit(this::loadLocations);

        try {
            categoryFuture.get();
            locationFuture.get();
        } catch (Exception e) {
            log.error("Error during data initialization", e);
            Thread.currentThread().interrupt();
        }

        log.info("Data initialization finished.");
    }

     void loadCategories() {
        try {
            semaphore.acquire();
            log.info("Requesting categories from the KudaGo API...");
            List<Category> categories = apiService.fetchCategories();
            categories.forEach(categoryDataStore::add);
            log.info("Categories have been successfully uploaded and saved. Total categories: {}", categories.size());
        } catch (Exception e) {
            log.error("Categories load error: {}", e.getMessage());
        } finally {
            semaphore.release();
        }
    }

    void loadLocations() {
        try {
            semaphore.acquire();
            log.info("Requesting locations from the KudaGo API...");
            List<Location> locations = apiService.fetchLocations();
            locations.forEach(locationDataStore::add);
            log.info("Locations have been successfully uploaded and saved. Total locations: {}", locations.size());
        } catch (Exception e) {
            log.error("Location load error: {}", e.getMessage());
        } finally {
            semaphore.release();
        }
    }
}
