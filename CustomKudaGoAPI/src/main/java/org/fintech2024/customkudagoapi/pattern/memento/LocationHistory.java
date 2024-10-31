package org.fintech2024.customkudagoapi.pattern.memento;

import org.fintech2024.customkudagoapi.model.Location;
import org.hibernate.annotations.Comment;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

@Component
public class LocationHistory {
    private final Map<Long, Stack<LocationMemento>> history = new HashMap<>();

    public void save(Long locationId, Location location) {
        history.putIfAbsent(locationId, new Stack<>());
        history.get(locationId).push(new LocationMemento(location));
    }

    public LocationMemento undo(Long locationId) {
        Stack<LocationMemento> locationHistory = history.get(locationId);
        if (locationHistory == null || locationHistory.isEmpty()) {
            return null;
        }
        return locationHistory.pop();
    }
}