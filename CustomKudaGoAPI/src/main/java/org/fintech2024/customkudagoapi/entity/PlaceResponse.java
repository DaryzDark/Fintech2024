package org.fintech2024.customkudagoapi.entity;

import lombok.Data;

import java.util.List;

@Data
public class PlaceResponse {
    private List<Place> results;
}
