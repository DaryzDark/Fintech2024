package org.fintech2024.customkudagoapi.pattern.memento;

import org.fintech2024.customkudagoapi.model.Location;

public class LocationMemento implements Memento<Location> {
    private final Location state;

    public LocationMemento(Location state) {
        this.state = new Location(state.getSlug(), 
                state.getName(), 
                state.getLanguage(), 
                state.getCoords(), 
                state.getTimezone());
    }

    @Override
    public Location getState() {
        return state;
    }
}
