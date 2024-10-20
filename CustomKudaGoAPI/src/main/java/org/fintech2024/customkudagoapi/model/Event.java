package org.fintech2024.customkudagoapi.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import org.fintech2024.customkudagoapi.config.PriceDeserializer;

import java.math.BigDecimal;

@Data
public class Event {
    private int id;
    private String title;
    private String description;
    @JsonDeserialize(using = PriceDeserializer.class)
    private BigDecimal price;
    private String site_url;
}
