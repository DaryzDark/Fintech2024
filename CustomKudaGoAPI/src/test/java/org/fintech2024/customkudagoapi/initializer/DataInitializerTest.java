package org.fintech2024.customkudagoapi.initializer;

import org.fintech2024.customkudagoapi.model.Category;
import org.fintech2024.customkudagoapi.model.Location;
import org.fintech2024.customkudagoapi.repository.InMemoryGenericDataStore;
import org.fintech2024.customkudagoapi.service.FetchKudaGoAPIService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DataInitializerTest {

    @Mock
    private FetchKudaGoAPIService apiService;

    @Mock
    private InMemoryGenericDataStore<Category> categoryDataStore;

    @Mock
    private InMemoryGenericDataStore<Location> locationDataStore;

    @InjectMocks
    private DataInitializer dataInitializer;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testInitDataSuccessfully() {
        List<Category> categories = Arrays.asList(new Category(), new Category());
        List<Location> locations = Arrays.asList(new Location(), new Location());

        when(apiService.fetchCategories()).thenReturn(categories);
        when(apiService.fetchLocations()).thenReturn(locations);

        dataInitializer.initData();

        for (Category category : categories) {
            verify(categoryDataStore).add(category);
        }
        for (Location location : locations) {
            verify(locationDataStore).add(location);
        }
    }

    @Test
    public void testInitDataHandlesCategoryLoadError() {
        when(apiService.fetchCategories()).thenThrow(new RuntimeException("API error"));

        dataInitializer.initData();

        verify(categoryDataStore, never()).add(any());
    }

    @Test
    public void testInitDataHandlesLocationLoadError() {
        List<Category> categories = Arrays.asList(new Category(), new Category());

        when(apiService.fetchCategories()).thenReturn(categories);
        when(apiService.fetchLocations()).thenThrow(new RuntimeException("API error"));

        dataInitializer.initData();

        for (Category category : categories) {
            verify(categoryDataStore).add(category);
        }
        verify(locationDataStore, never()).add(any());
    }
}