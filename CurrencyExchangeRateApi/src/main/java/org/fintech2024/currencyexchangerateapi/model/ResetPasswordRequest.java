package org.fintech2024.currencyexchangerateapi.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResetPasswordRequest {
    @Schema(description = "Email address", example = "jondoe@gmail.com")
    @Size(min = 5, max = 255, message = "Email address must be between 5 and 255 characters")
    @NotBlank(message = "Email address cannot be empty")
    @Email(message = "Email address must be in the format user@example.com")
    private String email;

    @NotBlank(message = "Token cannot be empty")
    private String token;

    @Schema(description = "Password", example = "my_1secret1_password")
    @Size(max = 255, message = "Password length must not exceed 255 characters")
    private String password;

    private String verificationCode;
}
