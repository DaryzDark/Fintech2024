package org.fintech2024.customkudagoapi.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Coords {
    @NotNull
    @NotBlank
    private double lat;
    @NotNull
    @NotBlank
    private double lon;
}
