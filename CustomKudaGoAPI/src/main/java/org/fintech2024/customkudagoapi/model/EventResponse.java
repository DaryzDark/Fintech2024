package org.fintech2024.customkudagoapi.model;

import lombok.Data;

import java.util.List;

@Data
public class EventResponse {
    private List<Event> results;
}
