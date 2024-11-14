package org.fintech2024.customkudagoapi.repository;

import org.fintech2024.customkudagoapi.exeption.EntityNotFoundException;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;


public class InMemoryGenericDataStore<T> {

    private final Map<Long, T> store = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong();

    public T add(T entity) {
        Long id = idGenerator.incrementAndGet();
        store.put(id, entity);
        return entity;
    }

    public List<T> getAll() {
        return new ArrayList<>(store.values());
    }

    public Optional<T> getById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    public Optional<T> update(Long id, T entity) {
        if (store.containsKey(id)) {
            store.put(id, entity);
            return Optional.of(entity);
        }
        return Optional.empty();
    }

    public void delete(Long id) {
        if (!store.containsKey(id)) {
            throw new EntityNotFoundException(id);
        }
        store.remove(id);
    }
}