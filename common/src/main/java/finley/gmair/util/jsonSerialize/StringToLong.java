package finley.gmair.util.jsonSerialize;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

/**
 * @Author Joby
 * @Date 10/20/2021 6:30 PM
 * @Description
 */
public class StringToLong extends JsonDeserializer<Long> {
    @Override
    public Long deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {

        return Long.valueOf(p.getText());
    }

}
