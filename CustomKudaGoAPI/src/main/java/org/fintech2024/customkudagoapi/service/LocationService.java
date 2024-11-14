package org.fintech2024.customkudagoapi.service;

import org.fintech2024.customkudagoapi.model.Location;
import org.fintech2024.customkudagoapi.repository.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocationService {

    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public List<Location> getAllLocations() {
        return locationRepository.getAll();
    }

    public Optional<Location> getLocationById(Long id) {
        return locationRepository.getById(id);
    }

    public Location addLocation(Location location) {
        return locationRepository.add(location);
    }

    public Optional<Location> updateLocation(Long id, Location location) {
        return locationRepository.update(id, location);
    }

    public void deleteLocation(Long id) {
        locationRepository.delete(id);
    }
}
