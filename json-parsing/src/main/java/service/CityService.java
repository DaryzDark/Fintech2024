package service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.City;

@Slf4j
@NoArgsConstructor
public class CityService {

    XmlMapper xmlMapper = new XmlMapper();

    public String toXML(City city) {
        try {
            return xmlMapper.writeValueAsString(city);
        } catch (JsonProcessingException e) {
            log.error("Object serialization to XML error: {}", e.getMessage());
            return "<error>Object serialization to XML error</error>";
        }
    }
}
