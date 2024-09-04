import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class City {

    @JsonProperty("slug")
    private String slug;

    @JsonProperty("coords")
    private Coords coords;

    public String toXML() {
        try {
            XmlMapper xmlMapper = new XmlMapper();
            return xmlMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            System.err.println("Object serialization to XML error: " + e.getMessage());
            return "<error>Object serialization to XML error</error>";
        }
    }
}
