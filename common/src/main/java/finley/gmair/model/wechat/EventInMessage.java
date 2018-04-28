package finley.gmair.model.wechat;

public class EventInMessage extends InMessage {
    private String Event;
    private String EventKey;

    public EventInMessage() {
        super();
    }

    public EventInMessage(String event, String eventKey) {
        this();
        this.Event = event;
        this.EventKey = eventKey;
    }

    public String getEvent() {
        return Event;
    }

    public void setEvent(String event) {
        Event = event;
    }

    public String getEventKey() {
        return EventKey;
    }

    public void setEventKey(String eventKey) {
        EventKey = eventKey;
    }
}