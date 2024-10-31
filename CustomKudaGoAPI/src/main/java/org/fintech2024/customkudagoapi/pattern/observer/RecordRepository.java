package org.fintech2024.customkudagoapi.pattern.observer;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class RecordRepository {

    private final List<String> records = new ArrayList<>();

    public void save(String record) {
        records.add(record);
    }

    public List<String> getAllRecords() {
        return new ArrayList<>(records);
    }
}
