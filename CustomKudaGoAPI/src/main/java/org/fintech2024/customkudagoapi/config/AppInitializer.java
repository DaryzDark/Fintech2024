package org.fintech2024.customkudagoapi.config;

import org.fintech2024.customkudagoapi.pattern.observer.RecordRepository;
import org.fintech2024.customkudagoapi.pattern.observer.RecordStorageObserver;
import org.fintech2024.customkudagoapi.service.CategoryService;
import org.fintech2024.customkudagoapi.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppInitializer {

    @Autowired
    public AppInitializer(CategoryService categoryService, LocationService locationService, RecordRepository recordRepository) {
        RecordStorageObserver recordStorageObserver = new RecordStorageObserver(recordRepository);
        categoryService.addObserver(recordStorageObserver);
        locationService.addObserver(recordStorageObserver);
    }
}
