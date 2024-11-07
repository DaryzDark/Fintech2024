package org.fintech2024.currencyexchangerateapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.fintech2024.currencyexchangerateapi.model.*;
import org.fintech2024.currencyexchangerateapi.repository.PasswordResetTokenRepository;
import org.fintech2024.currencyexchangerateapi.service.AuthenticationService;
import org.fintech2024.currencyexchangerateapi.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthController {
    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    @Operation(summary = "User Registration")
    @PostMapping("/sign-up")
    public JwtAuthenticationResponse signUp(@RequestBody @Valid SignUpRequest request) {
        return authenticationService.signUp(request);
    }

    @Operation(summary = "User Login")
    @PostMapping("/sign-in")
    public JwtAuthenticationResponse signIn(@RequestBody @Valid SignInRequest request) {
        return authenticationService.signIn(request);
    }

    @Operation(summary = "Logout")
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody @Valid LogoutRequest request) {
        authenticationService.logout(request.getToken());
        return ResponseEntity.ok("You have successfully logged out.");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody @Valid ForgotPasswordRequest request) {
        User user = userService.findByEmail(request.getEmail());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User with email " + request.getEmail() + " not found.");
        }
        String token = userService.createPasswordResetToken(user);
        // emailService.sendPasswordResetEmail(user.getEmail(), token);
        return ResponseEntity.ok("Password reset instructions have been sent to your email.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(request.getToken());
        if (resetToken == null || new Date(System.currentTimeMillis()).after(resetToken.getExpiryDate())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired token.");
        }
        if (!"0000".equals(request.getVerificationCode())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid verification code.");
        }
        User user = resetToken.getUser();
        authenticationService.updatePassword(user, request.getPassword());
        passwordResetTokenRepository.delete(resetToken);
        return ResponseEntity.ok("Password successfully changed.");
    }
}
