package finley.gmair.model.tmallGenie;

import finley.gmair.model.Entity;

public class AliGenieRe extends Entity {

    Header header;

    Payload payload;

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
