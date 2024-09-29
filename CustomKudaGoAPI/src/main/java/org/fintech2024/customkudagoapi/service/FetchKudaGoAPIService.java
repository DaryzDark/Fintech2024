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
}
