package org.fintech2024.customkudagoapi.service;

import org.fintech2024.customkudagoapi.model.Category;
import org.fintech2024.customkudagoapi.model.Location;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.wiremock.integrations.testcontainers.WireMockContainer;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
@ExtendWith(SpringExtension.class)
public class FetchKudaGoAPIServiceTest {

    @Container
    private static final WireMockContainer wireMockContainer =
            new WireMockContainer(DockerImageName.parse("wiremock/wiremock:latest"))
                    .withExposedPorts(8080);

    private FetchKudaGoAPIService fetchKudaGoAPIService;



    @BeforeEach
    public void setUp() {
        configureFor("localhost", wireMockContainer.getMappedPort(8080));
        fetchKudaGoAPIService = new FetchKudaGoAPIService();
    }
    @AfterAll
    public static void tearDown() {
        wireMockContainer.stop();
    }

    @Test
    public void fetchCategories_shouldReturnCategories() {
        String baseUrl = wireMockContainer.getBaseUrl();

        stubFor(
                get(urlEqualTo("/public-api/v1.4/place-categories/?fields=slug,name"))
                        .willReturn(aResponse()
                                .withHeader("Content-Type", "application/json")
                                .withStatus(200)
                                .withBody("[{\"slug\":\"ball\",\"name\":\"Балы (Развлечения)\"}," +
                                        "{\"slug\":\"business-events\",\"name\":\"События для бизнеса\"}]")));


        List<Category> categories = fetchKudaGoAPIService.fetchCategories(baseUrl);

        assertThat(categories).hasSize(2);
        assertThat(categories.get(0).getSlug()).isEqualTo("ball");
        assertThat(categories.get(0).getName()).isEqualTo("Балы (Развлечения)");
        assertThat(categories.get(1).getSlug()).isEqualTo("business-events");
        assertThat(categories.get(1).getName()).isEqualTo("События для бизнеса");
    }

    @Test
    public void fetchLocations_shouldReturnLocations() {
        String baseUrl = wireMockContainer.getBaseUrl();

        stubFor(get(urlEqualTo("/public-api/v1.4/locations/?fields=slug,name,timezone,coords,language"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("[{" +
                                "\"slug\": \"moscow\"," +
                                "\"name\": \"Moscow\"," +
                                "\"timezone\": \"Europe/Moscow\"," +
                                "\"coords\": {\"lat\": 55.7558, \"lon\": 37.6173}," +
                                "\"language\": \"ru\"" +
                                "}]")));

        List<Location> locations = fetchKudaGoAPIService.fetchLocations(baseUrl);

        assertThat(locations).hasSize(1);
        assertThat(locations.getFirst().getSlug()).isEqualTo("moscow");
        assertThat(locations.getFirst().getName()).isEqualTo("Moscow");
        assertThat(locations.getFirst().getTimezone()).isEqualTo("Europe/Moscow");
        assertThat(locations.getFirst().getLanguage()).isEqualTo("ru");

        assertThat(locations.getFirst().getCoords()).isNotNull();
        assertThat(locations.getFirst().getCoords().getLat()).isEqualTo(55.7558);
        assertThat(locations.getFirst().getCoords().getLon()).isEqualTo(37.6173);
    }
}