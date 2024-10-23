package org.fintech2024.customkudagoapi.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.fintech2024.customkudagoapi.entity.Event;
import org.fintech2024.customkudagoapi.entity.Place;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class EventDatesDeserializer extends JsonDeserializer<Event> {

    @Override
    public Event deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JsonProcessingException {

        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        Long id = node.get("id").asLong();

        String title = node.get("title").asText();

        String description = node.get("description").asText();

        JsonNode datesNode = node.get("dates").get(0);
        long startTimestamp = datesNode.get("start").asLong();
        long endTimestamp = datesNode.get("end").asLong();

        LocalDate startDate = Instant.ofEpochSecond(startTimestamp)
                .atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endDate = Instant.ofEpochSecond(endTimestamp)
                .atZone(ZoneId.systemDefault()).toLocalDate();

        Place place = null;
        if (node.has("place") && !node.get("place").isNull()) {
            JsonNode placeNode = node.get("place");
            place = new Place();
            place.setId(placeNode.get("id").asLong());
        }

        return new Event(id, title, description, startDate, endDate, place);
    }
}
