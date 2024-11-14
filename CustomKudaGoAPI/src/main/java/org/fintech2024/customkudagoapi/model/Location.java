package org.fintech2024.customkudagoapi.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Location {
    @NotNull
    @NotBlank
    private String slug;
    @NotNull
    @NotBlank
    private String name;
    @NotNull
    @NotBlank
    private String timezone;
    private Coords coords;
    @NotNull
    @NotBlank
    private String language;
}
