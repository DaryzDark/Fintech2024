package org.fintech2024.customkudagoapi.repository;

import org.fintech2024.customkudagoapi.exeption.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryGenericDataStoreTest {

    private InMemoryGenericDataStore<String> dataStore;

    @BeforeEach
    void setUp() {
        dataStore = new InMemoryGenericDataStore<>();
    }

    @Test
    void add_ShouldAddEntity_Positive() {
        String entity = "Test Entity";
        String result = dataStore.add(entity);

        assertEquals(entity, result);
        assertEquals(1, dataStore.getAll().size());
    }

    @Test
    void getAll_ShouldReturnAllEntities_Positive() {
        dataStore.add("Entity 1");
        dataStore.add("Entity 2");

        List<String> allEntities = dataStore.getAll();

        assertEquals(2, allEntities.size());
        assertTrue(allEntities.contains("Entity 1"));
        assertTrue(allEntities.contains("Entity 2"));
    }

    @Test
    void getById_ShouldReturnEntity_WhenExists_Positive() {
        Long id = 1L;
        dataStore.add("Entity");
        Optional<String> entity = dataStore.getById(id);

        assertTrue(entity.isPresent());
        assertEquals("Entity", entity.get());
    }

    @Test
    void getById_ShouldReturnEmptyOptional_WhenDoesNotExist_Negative() {
        Optional<String> entity = dataStore.getById(999L);

        assertFalse(entity.isPresent());
    }

    @Test
    void update_ShouldUpdateEntity_WhenExists_Positive() {
        Long id = 1L;
        dataStore.add("Entity");
        String newEntity = "Updated Entity";

        Optional<String> updatedEntity = dataStore.update(id, newEntity);

        assertTrue(updatedEntity.isPresent());
        assertEquals(newEntity, updatedEntity.get());
        assertEquals(1, dataStore.getAll().size());
        assertEquals(newEntity, dataStore.getById(id).get());
    }

    @Test
    void update_ShouldReturnEmptyOptional_WhenDoesNotExist_Negative() {
        Optional<String> updatedEntity = dataStore.update(999L, "New Entity");

        assertFalse(updatedEntity.isPresent());
    }

    @Test
    void delete_ShouldRemoveEntity_WhenExists_Positive() {
        Long id = 1L;
        dataStore.add("Entity"); // Получаем id

        dataStore.delete(id);

        assertEquals(0, dataStore.getAll().size());
    }

    @Test
    void delete_ShouldThrowEntityNotFoundException_WhenDoesNotExist_Negative() {
        Exception exception = assertThrows(EntityNotFoundException.class, () ->
                dataStore.delete(999L));

        String expectedMessage = "Entity not found with ID: 999";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }
}