package org.fintech2024.currencyexchangerateapi.exeption;

import lombok.Getter;

@Getter
public class CurrencyServiceUnavailableException extends RuntimeException {
    private final long retryAfterSeconds;

    public CurrencyServiceUnavailableException(String message, long retryAfterSeconds) {
        super(message);
        this.retryAfterSeconds = retryAfterSeconds;
    }

}
