package org.fintech2024.customkudagoapi.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

@Service
public class CurrencyConversionService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String cbrApiUrl = "http://www.cbr.ru/scripts/XML_daily.asp";

    public CompletableFuture<BigDecimal> convertToRubles(BigDecimal amount, String currency) {
        return CompletableFuture.supplyAsync(() -> {
            String response = restTemplate.getForObject(cbrApiUrl, String.class);

            BigDecimal exchangeRate = getCurrencyRateFromXML(response, currency);

            return exchangeRate.multiply(amount);
        });
    }

    private BigDecimal getCurrencyRateFromXML(String xmlResponse, String currencyCode) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new ByteArrayInputStream(xmlResponse.getBytes(StandardCharsets.UTF_8)));

            NodeList valuteList = document.getElementsByTagName("Valute");
            for (int i = 0; i < valuteList.getLength(); i++) {
                String charCode = valuteList.item(i).getChildNodes().item(1).getTextContent();
                if (charCode.equalsIgnoreCase(currencyCode)) {
                    String rateStr = valuteList.item(i).getChildNodes().item(4).getTextContent();
                    rateStr = rateStr.replace(",", ".");
                    return new BigDecimal(rateStr);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while parsing XML or finding currency rate", e);
        }
        throw new RuntimeException("Currency exchange rate for: " + currencyCode + " not found");
    }
}
