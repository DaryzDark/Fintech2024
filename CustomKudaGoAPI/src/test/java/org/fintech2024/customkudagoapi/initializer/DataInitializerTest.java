package org.fintech2024.customkudagoapi.initializer;

import org.fintech2024.customkudagoapi.model.Category;
import org.fintech2024.customkudagoapi.model.Location;
import org.fintech2024.customkudagoapi.repository.InMemoryGenericDataStore;
import org.fintech2024.customkudagoapi.service.FetchKudaGoAPIService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static org.mockito.Mockito.*;

class DataInitializerTest {

    @Mock
    private FetchKudaGoAPIService apiService;

    @Mock
    private InMemoryGenericDataStore<Category> categoryDataStore;

    @Mock
    private InMemoryGenericDataStore<Location> locationDataStore;

    @Mock
    private ExecutorService executorService;

    private DataInitializer dataInitializer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        dataInitializer = new DataInitializer(apiService, categoryDataStore, locationDataStore, executorService, 2);
    }

    @Test
    void testLoadCategoriesWithRateLimiting() {
        when(apiService.fetchCategories()).thenReturn(Collections.emptyList());

        Future<?> mockFuture = mock(Future.class);
        when(executorService.submit(any(Runnable.class))).thenAnswer(invocation -> {
            Runnable task = invocation.getArgument(0);
            task.run();
            return mockFuture;
        });

        dataInitializer.initData();

        verify(apiService, times(1)).fetchCategories();
        verify(executorService, times(2)).submit(any(Runnable.class));
    }

    @Test
    void testLoadLocationsWithRateLimiting() {
        when(apiService.fetchLocations()).thenReturn(Collections.emptyList());

        Future<?> mockFuture = mock(Future.class);
        when(executorService.submit(any(Runnable.class))).thenAnswer(invocation -> {
            Runnable task = invocation.getArgument(0);
            task.run();
            return mockFuture;
        });

        dataInitializer.initData();

        verify(apiService, times(1)).fetchLocations();
        verify(executorService, times(2)).submit(any(Runnable.class));
    }
}