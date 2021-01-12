package finley.gmair.model.minivoice;

import com.fasterxml.jackson.annotation.JsonInclude;
import finley.gmair.model.Entity;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MiniVoiceRequest extends Entity {

    Header header;

    RequestPayload requestPayload;

    public MiniVoiceRequest() {
        super();
    }

    public MiniVoiceRequest(Header header, RequestPayload requestPayload) {
        super();
        this.header = header;
        this.requestPayload = requestPayload;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public RequestPayload getRequestPayload() {
        return requestPayload;
    }

    public void setRequestPayload(RequestPayload requestPayload) {
        this.requestPayload = requestPayload;
    }

    @Override
    public String toString() {
        return "OpenPlatformRequest{" +
                "header=" + header +
                ", payload=" + requestPayload +
                '}';
    }

}
