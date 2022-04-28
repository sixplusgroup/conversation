package finley.gmair.model.drift;

import finley.gmair.model.Entity;

public class DriftChannel extends Entity {
    private String channelId;

    private String channelName;

    public DriftChannel() {
        super();
    }

    public DriftChannel(String channelName) {
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
