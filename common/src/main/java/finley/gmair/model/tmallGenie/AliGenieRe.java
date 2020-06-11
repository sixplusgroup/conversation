package finley.gmair.model.tmallGenie;

import com.fasterxml.jackson.annotation.JsonInclude;
import finley.gmair.model.Entity;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AliGenieRe extends Entity {

    Header header;

    Payload payload;

    public List<Attribute> getProperties() {
        return properties;
    }

    public void setProperties(List<Attribute> properties) {
        this.properties = properties;
    }

    List<Attribute> properties;

    public AliGenieRe() {
        super();
    }

    public AliGenieRe(Header header, Payload payload) {
        this.header = header;
        this.payload = payload;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Payload getPayload() {
        return payload;
    }

    public void setPayload(Payload payload) {
        this.payload = payload;
    }
}
