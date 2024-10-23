package org.fintech2024.customkudagoapi.controller;

import org.fintech2024.customkudagoapi.entity.Place;
import org.fintech2024.customkudagoapi.exeption.EntityNotFoundException;
import org.fintech2024.customkudagoapi.repository.PlaceRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/places")
public class PlaceController {

    private final PlaceRepository placeRepository;

    public PlaceController(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    @GetMapping
    public List<Place> getAllPlaces() {
        return placeRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Place> getPlaceById(@PathVariable Long id) {
        Place place = placeRepository.findByIdWithEvents(id)
                .orElseThrow(() -> new EntityNotFoundException("Place not found with ID: " + id));
        return ResponseEntity.ok(place);
    }

    // Создание нового места
    @PostMapping
    public ResponseEntity<Place> createPlace(@RequestBody Place place) {
        if (placeRepository.existsById(place.getId())) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok(placeRepository.save(place));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Place> updatePlace(@PathVariable Long id, @RequestBody Place updatedPlace) {
        Place place = placeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Place not found with ID: " + id));

        place.setTitle(updatedPlace.getTitle());
        place.setSlug(updatedPlace.getSlug());
        place.setDescription(updatedPlace.getDescription());

        return ResponseEntity.ok(placeRepository.save(place));
    }

    // Удаление места и связанных с ним событий (каскадное удаление)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlace(@PathVariable Long id) {
        if (!placeRepository.existsById(id)) {
            throw new EntityNotFoundException("Place not found with ID: " + id);
        }
        placeRepository.deleteById(id);
        return ResponseEntity.noContent().build();  // Возвращаем 204, если успешно удалено
    }
}
