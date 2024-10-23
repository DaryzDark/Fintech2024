package org.fintech2024.customkudagoapi.service;

import jakarta.transaction.Transactional;
import org.fintech2024.customkudagoapi.config.EventSpecification;
import org.fintech2024.customkudagoapi.entity.Event;
import org.fintech2024.customkudagoapi.entity.Place;
import org.fintech2024.customkudagoapi.repository.EventRepository;
import org.fintech2024.customkudagoapi.repository.PlaceRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final PlaceRepository placeRepository;

    public EventService(EventRepository eventRepository, PlaceRepository placeRepository) {
        this.eventRepository = eventRepository;
        this.placeRepository = placeRepository;
    }

    public List<Event> searchEvents(String name, Place place, Date fromDate, Date toDate) {
        return eventRepository.findAll(EventSpecification.filterEvents(name, place, fromDate, toDate));
    }

    @Transactional
    public void deletePlace(Long placeId) {
        placeRepository.deleteById(placeId);
    }

    @Transactional
    public void saveAllEvents(List<Event> events) {
        Place defaultPlace = placeRepository.findById(9999L)
                .orElseThrow(() -> new RuntimeException("Default place 'Not stated' not found"));

        events.forEach(event -> {
            if (event.getPlace() == null || placeRepository.findById(event.getPlace().getId()).isEmpty()) {
                event.setPlace(defaultPlace);
            }
        });

        eventRepository.saveAll(events);
    }

}
