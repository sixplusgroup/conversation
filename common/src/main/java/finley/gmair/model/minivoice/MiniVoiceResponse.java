package finley.gmair.model.minivoice;

import com.fasterxml.jackson.annotation.JsonInclude;
import finley.gmair.model.Entity;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MiniVoiceResponse extends Entity {

    Header header;

    ResponsePayload responsePayload;

    public MiniVoiceResponse() {
        super();
    }

    public MiniVoiceResponse(Header header, ResponsePayload responsePayload) {
        super();
        this.header = header;
        this.responsePayload = responsePayload;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public ResponsePayload getResponsePayload() {
        return responsePayload;
    }

    public void setResponsePayload(ResponsePayload responsePayload) {
        this.responsePayload = responsePayload;
    }

    @Override
    public String toString() {
        return "OpenPlatformRequest{" +
                "header=" + header +
                ", payload=" +  +
                '}';
    }

}
