package org.fintech2024.customkudagoapi.model;

import lombok.Data;

@Data
public class Location {
    private String slug;
    private String name;
    private String timezone;
    private Coords coords;
    private String language;
}
