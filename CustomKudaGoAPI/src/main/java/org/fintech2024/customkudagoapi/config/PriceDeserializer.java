package org.fintech2024.customkudagoapi.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.math.BigDecimal;

public class PriceDeserializer extends JsonDeserializer<BigDecimal> {
    @Override
    public BigDecimal deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String priceString = p.getText();

        priceString = priceString.replaceAll("[^0-9,.]", "").replace(",", ".");

        String[] parts = priceString.split("\\.");

        if (parts.length > 0 && !parts[0].isEmpty()) {
            return new BigDecimal(parts[0]);
        }

        return null;
    }
}