package org.fintech2024.customkudagoapi.service;

import org.fintech2024.customkudagoapi.model.Coords;
import org.fintech2024.customkudagoapi.model.Location;
import org.fintech2024.customkudagoapi.repository.LocationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LocationServiceTest {
    @Mock
    private LocationRepository locationRepository;

    @InjectMocks
    private LocationService locationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllLocations_ReturnsLocationList_Positive() {
        List<Location> mockLocations = List.of(
                new Location("park", "Park", "Europe/Moscow", new Coords(55.751244, 37.618423), "Russian"),
                new Location("museum", "Museum", "Europe/Moscow", new Coords(59.934280, 30.335099), "Russian")
        );
        when(locationRepository.getAll()).thenReturn(mockLocations);

        List<Location> result = locationService.getAllLocations();

        assertEquals(mockLocations, result);
        verify(locationRepository, times(1)).getAll();
    }

    @Test
    void getAllLocations_ReturnsEmptyList_Negative() {
        when(locationRepository.getAll()).thenReturn(List.of());

        List<Location> result = locationService.getAllLocations();

        assertTrue(result.isEmpty());
        verify(locationRepository, times(1)).getAll();
    }

    @Test
    void getLocationBySlug_ReturnsLocation_Positive() {
        Location mockLocation = new Location("park", "Park", "Europe/Moscow", new Coords(55.751244, 37.618423), "Russian");
        when(locationRepository.getById(1L)).thenReturn(Optional.of(mockLocation));

        Optional<Location> result = locationService.getLocationById(1L);

        assertTrue(result.isPresent());
        assertEquals(mockLocation, result.get());
        verify(locationRepository, times(1)).getById(1L);
    }

    @Test
    void getLocationBySlug_ReturnsEmpty_Negative() {
        when(locationRepository.getById(1L)).thenReturn(Optional.empty());

        Optional<Location> result = locationService.getLocationById(1L);

        assertFalse(result.isPresent());
        verify(locationRepository, times(1)).getById(1L);
    }

    @Test
    void addLocation_SavesLocation_Positive() {
        Location mockLocation = new Location("park", "Park", "Europe/Moscow", new Coords(55.751244, 37.618423), "Russian");
        when(locationRepository.add(mockLocation)).thenReturn(mockLocation);

        Location result = locationService.addLocation(mockLocation);

        assertEquals(mockLocation, result);
        verify(locationRepository, times(1)).add(mockLocation);
    }

    @Test
    void addLocation_ThrowsExceptionForExistingSlug_Negative() {
        Location mockLocation = new Location("park", "Park", "Europe/Moscow", new Coords(55.751244, 37.618423), "Russian");
        when(locationRepository.add(mockLocation)).thenThrow(new IllegalArgumentException("Location with this slug already exists"));

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                locationService.addLocation(mockLocation));

        assertEquals("Location with this slug already exists", exception.getMessage());
        verify(locationRepository, times(1)).add(mockLocation);
    }

    @Test
    void updateLocation_UpdatesLocation_Positive() {
        Location updatedLocation = new Location("park", "Updated Park", "Europe/Moscow", new Coords(55.751244, 37.618423), "Russian");
        when(locationRepository.update(1L, updatedLocation)).thenReturn(Optional.of(updatedLocation));

        Optional<Location> result = locationService.updateLocation(1L, updatedLocation);

        assertTrue(result.isPresent());
        assertEquals(updatedLocation, result.get());
        verify(locationRepository, times(1)).update(1L, updatedLocation);
    }

    @Test
    void updateLocation_ReturnsEmpty_Negative() {
        Location updatedLocation = new Location("park", "Updated Park", "Europe/Moscow", new Coords(55.751244, 37.618423), "Russian");
        when(locationRepository.update(1L, updatedLocation)).thenReturn(Optional.empty());

        Optional<Location> result = locationService.updateLocation(1L, updatedLocation);

        assertFalse(result.isPresent());
        verify(locationRepository, times(1)).update(1L, updatedLocation);
    }

    @Test
    void deleteLocation_DeletesLocation_Positive() {
        doNothing().when(locationRepository).delete(1L);

        locationService.deleteLocation(1L);

        verify(locationRepository, times(1)).delete(1L);
    }

    @Test
    void deleteLocation_ThrowsException_Negative() {
        doThrow(new IllegalArgumentException("Location not found")).when(locationRepository).delete(1L);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                locationService.deleteLocation(1L));

        assertEquals("Location not found", exception.getMessage());
        verify(locationRepository, times(1)).delete(1L);
    }

}