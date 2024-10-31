package org.fintech2024.customkudagoapi.service;

import org.fintech2024.customkudagoapi.model.Location;
import org.fintech2024.customkudagoapi.pattern.memento.LocationHistory;
import org.fintech2024.customkudagoapi.pattern.memento.LocationMemento;
import org.fintech2024.customkudagoapi.pattern.observer.Observer;
import org.fintech2024.customkudagoapi.pattern.observer.Subject;
import org.fintech2024.customkudagoapi.repository.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LocationService implements Subject {

    private final LocationRepository locationRepository;
    private final List<Observer> observers = new ArrayList<>();
    private final LocationHistory locationHistory;

    public LocationService(LocationRepository locationRepository, LocationHistory locationHistory) {
        this.locationRepository = locationRepository;
        this.locationHistory = locationHistory;
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }

    public List<Location> getAllLocations() {
        return locationRepository.getAll();
    }

    public Optional<Location> getLocationById(Long id) {
        return locationRepository.getById(id);
    }

    public Location addLocation(Location location) {
        Location addedLocation = locationRepository.add(location);
        notifyObservers("Location added: " + addedLocation);
        return addedLocation;
    }

    public Optional<Location> updateLocation(Long id, Location location) {
        Optional<Location> existingLocation = locationRepository.getById(id);
        existingLocation.ifPresent(loc -> locationHistory.save(id, loc));
        Optional<Location> updatedLocation = locationRepository.update(id, location);
        updatedLocation.ifPresent(loc -> notifyObservers("Location updated: " + loc));
        return updatedLocation;
    }

    public void deleteLocation(Long id) {
        locationRepository.getById(id).ifPresent(loc -> {
            locationHistory.save(id, loc);
            locationRepository.delete(id);
            notifyObservers("Location deleted: " + loc);
        });
    }

    public Optional<Location> restorePreviousState(Long id) {
        LocationMemento memento = locationHistory.undo(id);
        if (memento != null) {
            Location previousState = memento.getState();
            locationRepository.update(id, previousState);
            return Optional.of(previousState);
        }
        return Optional.empty();
    }
}
