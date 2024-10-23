package org.fintech2024.customkudagoapi.controller;

import org.fintech2024.customkudagoapi.entity.Event;
import org.fintech2024.customkudagoapi.entity.Place;
import org.fintech2024.customkudagoapi.exeption.EntityNotFoundException;
import org.fintech2024.customkudagoapi.exeption.RelatedEntityNotFoundException;
import org.fintech2024.customkudagoapi.repository.EventRepository;
import org.fintech2024.customkudagoapi.repository.PlaceRepository;
import org.fintech2024.customkudagoapi.service.EventService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventRepository eventRepository;
    private final PlaceRepository placeRepository;
    private final EventService eventService;

    public EventController(EventRepository eventRepository, PlaceRepository placeRepository, EventService eventService) {
        this.eventRepository = eventRepository;
        this.placeRepository = placeRepository;
        this.eventService = eventService;
    }

    @GetMapping
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event not found with ID: " + id));
        return ResponseEntity.ok(event);
    }

    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        if (event.getPlace() == null || !placeRepository.existsById(event.getPlace().getId())) {
            throw new RelatedEntityNotFoundException("Place with ID " + event.getPlace().getId() + " not found");
        }
        return ResponseEntity.ok(eventRepository.save(event));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @RequestBody Event updatedEvent) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event not found with ID: " + id));

        if (updatedEvent.getPlace() != null && !placeRepository.existsById(updatedEvent.getPlace().getId())) {
            throw new RelatedEntityNotFoundException("Place with ID " + updatedEvent.getPlace().getId() + " not found");
        }

        event.setTitle(updatedEvent.getTitle());
        event.setDescription(updatedEvent.getDescription());
        event.setStartDate(updatedEvent.getStartDate());
        event.setEndDate(updatedEvent.getEndDate());
        event.setPlace(updatedEvent.getPlace());

        return ResponseEntity.ok(eventRepository.save(event));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        if (!eventRepository.existsById(id)) {
            throw new EntityNotFoundException("Event not found with ID: " + id);
        }
        eventRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<Event>> searchEvents(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long placeId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate) {

        Place place = null;
        if (placeId != null) {
            place = placeRepository.findById(placeId).orElse(null);
        }

        List<Event> events = eventService.searchEvents(name, place, fromDate, toDate);

        return ResponseEntity.ok(events);
    }
}