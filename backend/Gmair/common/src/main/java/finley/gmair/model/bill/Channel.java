package finley.gmair.model.bill;

import finley.gmair.model.Entity;

public class Channel extends Entity {

    private String channelId;

    private String channelName;

    public Channel() {
        super();
    }

    public Channel(String channelName) {
        this();
        this.channelName = channelName;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

}
