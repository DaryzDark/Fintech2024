package org.fintech2024.currencyexchangerateapi.exeption;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MissingParameterException.class)
    public ResponseEntity<Map<String, Object>> handleMissingParameterException(MissingParameterException ex) {
        return createErrorResponse(HttpStatus.BAD_REQUEST, 400, ex.getMessage());
    }

    @ExceptionHandler(InvalidAmountException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidAmountException(InvalidAmountException ex) {
        return createErrorResponse(HttpStatus.BAD_REQUEST, 400, ex.getMessage());
    }

    @ExceptionHandler(CurrencyNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleCurrencyNotFoundException(CurrencyNotFoundException ex) {
        return createErrorResponse(HttpStatus.NOT_FOUND, 404, ex.getMessage());
    }

    @ExceptionHandler(CurrencyServiceUnavailableException.class)
    public ResponseEntity<Map<String, Object>> handleCurrencyServiceUnavailableException(CurrencyServiceUnavailableException ex, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Retry-After", String.valueOf(ex.getRetryAfterSeconds()));

        return createErrorResponse(HttpStatus.SERVICE_UNAVAILABLE, 503, ex.getMessage(), headers);
    }

    private ResponseEntity<Map<String, Object>> createErrorResponse(HttpStatus status, int code, String message) {
        return createErrorResponse(status, code, message, new HttpHeaders());
    }

    private ResponseEntity<Map<String, Object>> createErrorResponse(HttpStatus status, int code, String message, HttpHeaders headers) {
        Map<String, Object> body = new HashMap<>();
        body.put("code", code);
        body.put("message", message);
        return new ResponseEntity<>(body, headers, status);
    }
}
