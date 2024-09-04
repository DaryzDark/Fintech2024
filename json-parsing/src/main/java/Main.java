import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

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

        if (inputStream == null) {
            log.error("File not found!");
            throw new IllegalArgumentException("File not found!");
        }
        if (inputStreamError == null) {
            log.error("File not found!");
            throw new IllegalArgumentException("File not found!");
        }

        // Correct file parsing
        City city = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            city = objectMapper.readValue(inputStream, City.class);
        } catch (JsonParseException e) {
            log.error("Bad JSON syntax: {}", e.getMessage());
        } catch (JsonMappingException e) {
            log.error("Bad JSON mapping: {}", e.getMessage());
        } catch (IOException e) {
            log.error("File read error: {}", e.getMessage());
        }

        try {
            if (city != null) {
                String filePath = "city.xml";
                File file = new File(filePath);
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(city.toXML());
                fileWriter.close();

                log.info("File successfully saved at: {}", file.getAbsolutePath());
            } else {
                throw new IllegalArgumentException("Object is null and can't be written to file!");
            }
        } catch (IllegalArgumentException e) {
            log.error("Error: {}", e.getMessage());
        } catch (IOException e) {
            log.error("I/O error when writing XML to a file: {}", e.getMessage());
        }

        // Incorrect file parsing
        City cityError = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            cityError = objectMapper.readValue(inputStreamError, City.class);
        } catch (JsonParseException e) {
            log.error("Bad JSON syntax: {}", e.getMessage());
        } catch (JsonMappingException e) {
            log.error("Bad JSON mapping: {}", e.getMessage());
        } catch (IOException e) {
            log.error("File read error: {}", e.getMessage());
        }

        try {
            if (cityError != null) {
                String filePath = "cityError.xml";
                File file = new File(filePath);
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(cityError.toXML());
                fileWriter.close();

                log.info("File successfully saved at: {}", file.getAbsolutePath());
            } else {
                throw new IllegalArgumentException("Object is null and can't be written to file!");
            }
        } catch (IllegalArgumentException e) {
            log.error("Error: {}", e.getMessage());
        } catch (IOException e) {
            log.error("I/O error when writing XML to a file: {}", e.getMessage());
        }
    }
}
