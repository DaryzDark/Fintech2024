import com.fasterxml.jackson.databind.ObjectMapper;
import model.City;
import model.Coords;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.CityService;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MainTest {

    private InputStream inputStream;
    private InputStream inputStreamError;
    private ObjectMapper objectMapper;
    private CityService cityService;
    private final String cityFilePath = "city.xml";
    private final String cityErrorFilePath = "cityError.xml";

    @BeforeEach
    public void setUp() {
        inputStream = mock(InputStream.class);
        inputStreamError = mock(InputStream.class);
        objectMapper = mock(ObjectMapper.class);
        cityService = mock(CityService.class);
    }

    @AfterEach
    public void tearDown() {
        File cityFile = new File(cityFilePath);
        if (cityFile.exists()) {
            assertTrue(cityFile.delete(), "city.xml should be deleted after the test");
        }

        File cityErrorFile = new File(cityErrorFilePath);
        if (cityErrorFile.exists()) {
            assertTrue(cityErrorFile.delete(), "cityError.xml should be deleted after the test");
        }
    }

    @Test
    public void testRun_SuccessfullyParsesAndSavesCity() throws Exception {
        City mockCity = new City("spb", new Coords(59.939095, 30.315868));

        when(objectMapper.readValue(inputStream, City.class)).thenReturn(mockCity);
        String expectedXML = "<City><slug>spb</slug><coords><lat>59.939095</lat><lon>30.315868</lon></coords></City>";
        when(cityService.toXML(mockCity)).thenReturn(expectedXML);

        Main main = new Main();
        main.run(inputStream, inputStreamError, objectMapper, cityService);

        verify(objectMapper, times(1)).readValue(inputStream, City.class);
        verify(cityService, times(1)).toXML(mockCity);

    }

    @Test
    public void testRun_HandlesInvalidCityJSON() throws Exception {
        when(objectMapper.readValue(inputStreamError, City.class)).thenThrow(new IOException("Invalid JSON"));

        Main main = new Main();
        main.run(inputStream, inputStreamError, objectMapper, cityService);

        verify(objectMapper, times(1)).readValue(inputStreamError, City.class);
    }

    @Test
    public void testRun_FileNotFound_ThrowsException() {
        Main main = new Main();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            main.run(null, null, objectMapper, cityService);
        });
        assertEquals("File not found!", exception.getMessage());
    }
}