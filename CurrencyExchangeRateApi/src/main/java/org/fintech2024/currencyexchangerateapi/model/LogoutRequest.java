package org.fintech2024.currencyexchangerateapi.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LogoutRequest {
    @NotBlank(message = "Token cannot be empty")
    private String token;
}
