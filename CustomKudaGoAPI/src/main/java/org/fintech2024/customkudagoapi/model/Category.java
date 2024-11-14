package org.fintech2024.customkudagoapi.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Category {
    @NotNull
    @NotBlank
    private String slug;
    @NotNull
    @NotBlank
    private String name;
}
