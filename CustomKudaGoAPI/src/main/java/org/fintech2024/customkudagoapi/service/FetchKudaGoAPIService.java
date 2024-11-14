package org.fintech2024.customkudagoapi.service;

import org.fintech2024.customkudagoapi.model.Category;
import org.fintech2024.customkudagoapi.model.Location;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class FetchKudaGoAPIService {

    private final RestTemplate restTemplate = new RestTemplate();



    public List<Category> fetchCategories(String baseUrl) {
        String urlParams = "?fields=slug,name";
        String url = baseUrl + "/public-api/v1.4/place-categories/" + urlParams;
        Category[] categories = restTemplate.getForObject(url, Category[].class);
        return Arrays.asList(Objects.requireNonNull(categories));
    }

    public List<Location> fetchLocations(String baseUrl) {
        String urlParams = "?fields=slug,name,timezone,coords,language";
        String url = baseUrl + "/public-api/v1.4/locations/" + urlParams;
        Location[] locations = restTemplate.getForObject(url, Location[].class);
        return Arrays.asList(Objects.requireNonNull(locations));
    }
}
