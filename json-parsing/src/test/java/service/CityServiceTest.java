package service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import model.City;
import model.Coords;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
class CityServiceTest {
    @Mock
    private XmlMapper xmlMapper;

    @InjectMocks
    private CityService cityService;

    @BeforeEach
    public void setUp() {
            MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testToXML_Success() throws Exception {
        City city = new City("spb", new Coords(59.939095, 30.315868));
        String expectedXML = "<City><slug>spb</slug><coords><lat>59.939095</lat><lon>30.315868</lon></coords></City>";

        when(xmlMapper.writeValueAsString(city)).thenReturn(expectedXML);

        String actualXML = cityService.toXML(city);

        assertNotNull(actualXML);
        assertEquals(expectedXML, actualXML);

        verify(xmlMapper, times(1)).writeValueAsString(city);
    }

    @Test
    public void testToXML_JsonProcessingException() throws Exception {
        City city = new City("spb", new Coords(59.939095, 30.315868));
        when(xmlMapper.writeValueAsString(city)).thenThrow(new JsonProcessingException("Error serializing") {});

        String result = cityService.toXML(city);

        assertNotNull(result);
        assertEquals("<error>Object serialization to XML error</error>", result);

        verify(xmlMapper, times(1)).writeValueAsString(city);
    }
}