package org.fintech2024.customkudagoapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Location {
    private String slug;
    private String name;
    private String timezone;
    private Coords coords;
    private String language;
}
