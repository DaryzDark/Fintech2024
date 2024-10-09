package org.fintech2024.currencyexchangerateapi.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.fintech2024.currencyexchangerateapi.exeption.CurrencyServiceUnavailableException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import reactor.core.publisher.Mono;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class CurrencyCacheService {

    private final WebClient webClient;
    private final String apiUrl;

    public CurrencyCacheService(WebClient webClient, @Value("${currency-api.url}")  String apiUrl) {
        this.apiUrl = apiUrl;
        this.webClient = webClient;
    }

    @CircuitBreaker(name = "currencyService", fallbackMethod = "fallbackRates")
    @Cacheable(value = "currencyRates", key = "#date", cacheManager = "cacheManager")
    public Map<String, BigDecimal> getRates(String date) {
        String url = apiUrl + "?date_req=" + date;
        try {
            // Блокирующий вызов для синхронного выполнения
            String xmlResponse = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block(); // Используем блокирующий вызов для упрощения синхронного метода

            // Парсинг XML ответа в Map с курсами валют
            return parseRatesFromXml(xmlResponse);
        } catch (Exception ex) {
            // Если произошла ошибка, выбрасываем специальное исключение
            throw new CurrencyServiceUnavailableException("Currency service is unavailable", 3600);
        }
    }

    private Map<String, BigDecimal> parseRatesFromXml(String xml) {
        Map<String, BigDecimal> rates = new HashMap<>();
        try {
            // Создание документа XML из строки
            Document doc = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder()
                    .parse(new InputSource(new StringReader(xml)));

            // Получение всех узлов <Valute>
            NodeList valuteNodes = doc.getElementsByTagName("Valute");

            // Проход по всем узлам <Valute>
            for (int i = 0; i < valuteNodes.getLength(); i++) {
                Element valuteElement = (Element) valuteNodes.item(i);

                // Извлечение значения валюты <CharCode> и курса <VunitRate>
                String charCode = valuteElement.getElementsByTagName("CharCode").item(0).getTextContent();
                String vunitRateStr = valuteElement.getElementsByTagName("VunitRate").item(0).getTextContent();

                // Заменяем запятую на точку и преобразуем в BigDecimal
                BigDecimal vunitRate = new BigDecimal(vunitRateStr.replace(",", "."));

                // Сохраняем в карту (ключ - код валюты, значение - курс)
                rates.put(charCode, vunitRate);
            }
            rates.put("RUB", BigDecimal.ONE);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse XML", e);
        }
        return rates;
    }

    public Mono<Map<String, BigDecimal>> fallbackRates(String date, Throwable throwable) {
        System.err.println("Error occurred while fetching rates: " + throwable.getMessage());
        return Mono.just(new HashMap<>());
    }
}
