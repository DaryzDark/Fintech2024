import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import model.City;
import service.CityService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class Main {

    public static void main(String[] args) {
        ClassLoader classLoader = Main.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("city.json");
        InputStream inputStreamError = classLoader.getResourceAsStream("city-error.json");

        Main main = new Main();
        main.run(inputStream, inputStreamError, new ObjectMapper(), new CityService());
    }

    public void run(InputStream inputStream, InputStream inputStreamError, ObjectMapper objectMapper, CityService cityService) {
        if (inputStream == null || inputStreamError == null) {
            log.error("File not found!");
            throw new IllegalArgumentException("File not found!");
        } else {
            // Correct file parsing
            City city = parseCity(inputStream, objectMapper);

            if (city != null) {
                saveCityToFile(city, "city.xml", cityService);
            }

            // Incorrect file parsing
            City cityError = parseCity(inputStreamError, objectMapper);

            if (cityError != null) {
                saveCityToFile(cityError, "cityError.xml", cityService);
            }
        }
    }

    private City parseCity(InputStream inputStream, ObjectMapper objectMapper) {
        City city = null;
        try {
            city = objectMapper.readValue(inputStream, City.class);
        } catch (JsonParseException e) {
            log.error("Bad JSON syntax: {}", e.getMessage());
        } catch (JsonMappingException e) {
            log.error("Bad JSON mapping: {}", e.getMessage());
        } catch (IOException e) {
            log.error("File read error: {}", e.getMessage());
        }
        return city;
    }

    private void saveCityToFile(City city, String filePath, CityService cityService) {
        try {
            String xmlContent = cityService.toXML(city);
            File file = new File(filePath);
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(xmlContent);
            fileWriter.close();
            log.info("File successfully saved at: {}", file.getAbsolutePath());
        } catch (IOException e) {
            log.error("I/O error when writing XML to a file: {}", e.getMessage());
        }
    }
}
