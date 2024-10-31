package org.fintech2024.currencyexchangerateapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class CurrencyExchangeRateApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CurrencyExchangeRateApiApplication.class, args);
    }

}
