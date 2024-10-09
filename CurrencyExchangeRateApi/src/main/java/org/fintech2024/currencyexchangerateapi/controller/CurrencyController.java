package org.fintech2024.currencyexchangerateapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.fintech2024.currencyexchangerateapi.model.ConversionRequest;
import org.fintech2024.currencyexchangerateapi.service.CurrencyService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/currencies")
public class CurrencyController {
    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @Operation(summary = "Get currency rate", description = "Retrieves the exchange rate for today from the Central Bank.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Rates successfully retrieved. The response contains the exchange rate for the specified currency.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = """
                {
                    "currency": "USD",
                    "rate": 74.85
                }
                """)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Currency not found", content = @Content),
            @ApiResponse(responseCode = "503", description = "Currency service unavailable", content = @Content)
    })
    @GetMapping("/rates/{code}")
    public Map<String, Object> getCurrencyRate(@Parameter(
            description = "The ISO 4217 currency code",
            required = true,
            example = "USD"
    ) @PathVariable String code) {
        return currencyService.getCurrencyRate(code);
    }


    @Operation(
            summary = "Convert currency",
            description = "This endpoint converts the specified amount from one currency to another using the Central Bank's exchange rates."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Conversion successfully completed",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = """
                {
                    "fromCurrency": "USD",
                    "toCurrency": "RUB",
                    "convertedAmount": 7450.75
                }
                """)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request parameters. Required parameters are missing or the amount is less than or equal to zero.",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Currency not found. The currency code provided does not exist in the Central Bank's exchange rates.",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "503",
                    description = "Currency service unavailable. The service is currently unavailable, and the request could not be processed. Retry later.",
                    content = @Content(mediaType = "application/json")
            )
    })
    @PostMapping("/convert")
    public Map<String, Object> convertCurrency(@RequestBody ConversionRequest request) {
        return currencyService.convertCurrency(request.getFromCurrency(), request.getToCurrency(), request.getAmount());
    }
}
