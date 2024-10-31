package org.fintech2024.currencyexchangerateapi.exeption;

public class MissingParameterException extends RuntimeException {
    public MissingParameterException(String message) {
        super(message);
    }
}
