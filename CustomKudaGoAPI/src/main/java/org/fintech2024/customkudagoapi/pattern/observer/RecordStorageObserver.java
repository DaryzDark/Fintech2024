package org.fintech2024.customkudagoapi.pattern.observer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RecordStorageObserver implements Observer {

    private final RecordRepository recordRepository;

    public RecordStorageObserver(RecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    @Override
    public void update(String message) {
        recordRepository.save(message);
        log.info("Record saved: {}", message);
    }
}
