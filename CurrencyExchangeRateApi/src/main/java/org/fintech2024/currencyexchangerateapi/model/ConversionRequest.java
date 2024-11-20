package org.fintech2024.currencyexchangerateapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ConversionRequest {
    private String fromCurrency;
    private String toCurrency;
    private BigDecimal amount;
}
