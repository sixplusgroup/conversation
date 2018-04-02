package finley.gmair.model.order;

import finley.gmair.model.Entity;

public class OrderChannel extends Entity {
    private String channelId;

    private String channelName;

    public OrderChannel() {
        super();
    }

    public OrderChannel(String channelName) {
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
