package org.fintech2024.customkudagoapi.service;

import lombok.extern.slf4j.Slf4j;
import org.fintech2024.customkudagoapi.entity.EventResponse;
import org.fintech2024.customkudagoapi.entity.PlaceResponse;
import org.fintech2024.customkudagoapi.model.Category;
import org.fintech2024.customkudagoapi.model.Location;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class FetchKudaGoAPIService {

    private final RestTemplate restTemplate = new RestTemplate();

    public List<Category> fetchCategories() {
        String url_params = "?fields=slug,name";
        String url = "https://kudago.com/public-api/v1.4/place-categories/" + url_params;
        Category[] categories = restTemplate.getForObject(url, Category[].class);
        return Arrays.asList(Objects.requireNonNull(categories));
    }

    public List<Location> fetchLocations() {
        String url_params = "?fields=slug,name,timezone,coords,language";
        String url = "https://kudago.com/public-api/v1.4/locations/" + url_params;
        Location[] locations = restTemplate.getForObject(url, Location[].class);
        return Arrays.asList(Objects.requireNonNull(locations));
    }

    public PlaceResponse fetchPlaces() {
        String url_params = "?fields=id,title,slug,description&page_size=100";
        String url = "https://kudago.com/public-api/v1.4/places/" + url_params;
        PlaceResponse response = restTemplate.getForObject(url, PlaceResponse.class);
        return  Objects.requireNonNull(response);
    }

    public EventResponse fetchEvents() {
        String url_params = "?fields=id,title,description,dates,place";
        String url = "https://kudago.com/public-api/v1.4/events/" + url_params;
        EventResponse events = restTemplate.getForObject(url, EventResponse.class);
        return  Objects.requireNonNull(events);
    }
}
