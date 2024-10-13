package org.fintech2024.currencyexchangerateapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.fintech2024.currencyexchangerateapi.exeption.CurrencyNotFoundException;
import org.fintech2024.currencyexchangerateapi.exeption.CurrencyServiceUnavailableException;
import org.fintech2024.currencyexchangerateapi.model.ConversionRequest;
import org.fintech2024.currencyexchangerateapi.service.CurrencyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CurrencyController.class)
class CurrencyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CurrencyService currencyService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getCurrencyRate_Success() throws Exception {
        Map<String, Object> mockResponse = Map.of("currency", "USD", "rate", 74.85);
        when(currencyService.getCurrencyRate(anyString())).thenReturn(mockResponse);

        // Performing a GET request and validating the response
        mockMvc.perform(get("/currencies/rates/USD")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(mockResponse)));
    }

    @Test
    void getCurrencyRate_NotFound() throws Exception {
        when(currencyService.getCurrencyRate(anyString())).thenThrow(new CurrencyNotFoundException("Currency code USD not found"));

        mockMvc.perform(get("/currencies/rates/INVALID")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getCurrencyRate_ServiceUnavailable() throws Exception {
        when(currencyService.getCurrencyRate(anyString())).thenThrow(new CurrencyServiceUnavailableException("Service unavailable", 3600));

        // Performing a GET request and validating the response
        mockMvc.perform(get("/currencies/rates/USD")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isServiceUnavailable());
    }

    @Test
    void convertCurrency_Success() throws Exception {
        Map<String, Object> mockResponse = Map.of(
                "fromCurrency", "USD",
                "toCurrency", "RUB",
                "convertedAmount", 7450.75
        );
        when(currencyService.convertCurrency(any(String.class), any(String.class), any())).thenReturn(mockResponse);

        ConversionRequest conversionRequest = new ConversionRequest("USD", "RUB", BigDecimal.valueOf(100.5));

        mockMvc.perform(post("/currencies/convert")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(conversionRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(mockResponse)));
    }

    @Test
    void convertCurrency_NotFound() throws Exception {
        when(currencyService.convertCurrency(any(String.class), any(String.class), any()))
                .thenThrow(new CurrencyNotFoundException("Currency code USD not found"));

        ConversionRequest conversionRequest = new ConversionRequest("USD", "RUB", BigDecimal.valueOf(100.5));

        mockMvc.perform(post("/currencies/convert")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(conversionRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void convertCurrency_ServiceUnavailable() throws Exception {
        when(currencyService.convertCurrency(any(String.class), any(String.class), any(BigDecimal.class)))
                .thenThrow(new CurrencyServiceUnavailableException("Service unavailable", 3600));

        ConversionRequest conversionRequest = new ConversionRequest("USD", "RUB", BigDecimal.valueOf(100.5));

        mockMvc.perform(post("/currencies/convert")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(conversionRequest)))
                .andExpect(status().isServiceUnavailable());
    }
}