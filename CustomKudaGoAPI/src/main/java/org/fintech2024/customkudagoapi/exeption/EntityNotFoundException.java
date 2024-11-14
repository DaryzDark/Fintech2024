package org.fintech2024.customkudagoapi.exeption;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(Long id) {
        super("Entity not found with ID: " + id);
    }

}
