package org.fintech2024.currencyexchangerateapi.exeption;

public class InvalidAmountException extends RuntimeException {
    public InvalidAmountException(String message) {
        super(message);
    }
}
