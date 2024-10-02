package org.fintech2024.customkudagoapi.controller;

import org.fintech2024.customkudagoapi.exeption.EntityNotFoundException;
import org.fintech2024.customkudagoapi.model.Location;
import org.fintech2024.customkudagoapi.service.LocationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LocationController.class)
class LocationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LocationService locationService;

    private Location mockLocation;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockLocation = new Location("city-1", "City 1", "UTC+0", null, "en");
    }

    @Test
    void getAllLocations_ReturnsLocationList_Positive() throws Exception {
        List<Location> mockLocations = List.of(mockLocation);
        when(locationService.getAllLocations()).thenReturn(mockLocations);

        mockMvc.perform(get("/api/v1/locations")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(mockLocations.size()))
                .andExpect(jsonPath("$[0].slug").value("city-1"))
                .andExpect(jsonPath("$[0].name").value("City 1"));

        verify(locationService, times(1)).getAllLocations();
    }

    @Test
    void getAllLocations_ReturnsEmptyList_Negative() throws Exception {
        when(locationService.getAllLocations()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/locations")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));

        verify(locationService, times(1)).getAllLocations();
    }

    @Test
    void getLocationById_ReturnsLocation_Positive() throws Exception {
        when(locationService.getLocationById(1L)).thenReturn(Optional.of(mockLocation));

        mockMvc.perform(get("/api/v1/locations/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.slug").value("city-1"))
                .andExpect(jsonPath("$.name").value("City 1"));

        verify(locationService, times(1)).getLocationById(1L);
    }

    @Test
    void getLocationById_ReturnsNotFound_Negative() throws Exception {
        when(locationService.getLocationById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/locations/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(locationService, times(1)).getLocationById(1L);
    }

    @Test
    void createLocation_ReturnsCreatedLocation_Positive() throws Exception {
        when(locationService.addLocation(any(Location.class))).thenReturn(mockLocation);

        mockMvc.perform(post("/api/v1/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"slug\": \"city-1\", \"name\": \"City 1\", \"timezone\": \"UTC+0\", \"language\": \"en\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.slug").value("city-1"))
                .andExpect(jsonPath("$.name").value("City 1"));

        verify(locationService, times(1)).addLocation(any(Location.class));
    }

    @Test
    void createLocation_ReturnsBadRequest_Negative() throws Exception {
        mockMvc.perform(post("/api/v1/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"City 1\"}")) // Отсутствует поле slug
                .andExpect(status().isBadRequest());

        verify(locationService, never()).addLocation(any(Location.class));
    }

    // Позитивный сценарий: обновление локации
    @Test
    void updateLocation_ReturnsUpdatedLocation_Positive() throws Exception {
        Location updatedLocation = new Location("city-1", "Updated City 1", "UTC+0", null, "en");
        when(locationService.updateLocation(eq(1L), any(Location.class))).thenReturn(Optional.of(updatedLocation));

        mockMvc.perform(put("/api/v1/locations/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"slug\": \"city-1\", \"name\": \"Updated City 1\", \"timezone\": \"UTC+0\", \"language\": \"en\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.slug").value("city-1"))
                .andExpect(jsonPath("$.name").value("Updated City 1"));

        verify(locationService, times(1)).updateLocation(eq(1L), any(Location.class));
    }

    @Test
    void updateLocation_ReturnsNotFound_Negative() throws Exception {
        when(locationService.updateLocation(eq(1L), any(Location.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/v1/locations/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"slug\": \"city-1\", \"name\": \"Updated City 1\", \"timezone\": \"UTC+0\", \"language\": \"en\"}"))
                .andExpect(status().isNotFound());

        verify(locationService, times(1)).updateLocation(eq(1L), any(Location.class));
    }

    @Test
    void deleteLocation_ReturnsNoContent_Positive() throws Exception {
        doNothing().when(locationService).deleteLocation(1L);

        mockMvc.perform(delete("/api/v1/locations/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(locationService, times(1)).deleteLocation(1L);
    }

    @Test
    void deleteLocation_ReturnsNotFound_Negative() throws Exception {
        doThrow(new EntityNotFoundException(1L))
                .when(locationService).deleteLocation(1L);

        mockMvc.perform(delete("/api/v1/locations/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(locationService, times(1)).deleteLocation(1L);
    }
}