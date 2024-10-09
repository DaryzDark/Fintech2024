package org.fintech2024.currencyexchangerateapi.service;

import org.fintech2024.currencyexchangerateapi.exeption.CurrencyNotFoundException;
import org.fintech2024.currencyexchangerateapi.exeption.InvalidAmountException;
import org.fintech2024.currencyexchangerateapi.exeption.MissingParameterException;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
public class CurrencyService {

    private final CurrencyCacheService currencyCacheService;

    public CurrencyService(CurrencyCacheService currencyCacheService) {
        this.currencyCacheService = currencyCacheService;
    }

    public Map<String, Object> getCurrencyRate(String code) {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        Map<String, BigDecimal> rates = currencyCacheService.getRates(date);
        checkIfParameterIsValid("code", code);
        BigDecimal rate = rates.getOrDefault(code, BigDecimal.ZERO);
        if (rate.equals(BigDecimal.ZERO)) {
            throw new CurrencyNotFoundException("Currency not found: " + code);
        }
        return Map.of("currency", code, "rate", rate);
    }

    public Map<String, Object> convertCurrency(String fromCurrency, String toCurrency, BigDecimal amount) {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        Map<String, BigDecimal> rates = currencyCacheService.getRates(date);

        validateParameters(fromCurrency, toCurrency, amount);

        BigDecimal fromRate = rates.getOrDefault(fromCurrency, BigDecimal.ZERO);
        BigDecimal toRate = rates.getOrDefault(toCurrency, BigDecimal.ZERO);

        if (toRate.equals(BigDecimal.ZERO)) {
            throw new CurrencyNotFoundException("Currency not found: " + toCurrency);
        }
        if (fromRate.equals(BigDecimal.ZERO)) {
            throw new CurrencyNotFoundException("Currency not found: " + fromCurrency);
        }

        BigDecimal convertedAmount = amount.multiply(fromRate).divide(toRate, 4, RoundingMode.HALF_UP);
        return Map.of(
                "fromCurrency", fromCurrency,
                "toCurrency", toCurrency,
                "convertedAmount", convertedAmount
        );
    }


    private void validateParameters(String fromCurrency, String toCurrency, BigDecimal amount) {
        // Проверка на отсутствие и пустоту параметров
        checkIfParameterIsValid("fromCurrency", fromCurrency);
        checkIfParameterIsValid("toCurrency", toCurrency);

        // Проверка суммы на null и значение <= 0
        if (amount == null) {
            throw new MissingParameterException("Missing parameter: amount");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Amount must be greater than 0");
        }
    }

    private void checkIfParameterIsValid(String paramName, String paramValue) {
        if (paramValue == null || paramValue.isEmpty()) {
            throw new MissingParameterException("Missing parameter: " + paramName);
        }
    }
}

