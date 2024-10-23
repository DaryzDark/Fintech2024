package org.fintech2024.customkudagoapi.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import org.fintech2024.customkudagoapi.config.EventDatesDeserializer;

import java.util.List;

@Data
public class EventResponse {
    private List<Event> results;
}
