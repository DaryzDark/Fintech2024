package org.fintech2024.currencyexchangerateapi.exeption;

public class CurrencyNotFoundException extends RuntimeException {
    public CurrencyNotFoundException(String message) {
        super(message);
    }
}