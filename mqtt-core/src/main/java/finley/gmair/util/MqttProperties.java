package finley.gmair.util;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "com.mqtt")
public class MqttProperties {
    private MqttInbound inbound;
    private MqttOutbound outbound;

    public MqttInbound getInbound() {
        return inbound;
    }

    public void setInbound(MqttInbound inbound) {
        this.inbound = inbound;
    }

    public MqttOutbound getOutbound() {
        return outbound;
    }

    public void setOutbound(MqttOutbound outbound) {
        this.outbound = outbound;
    }
}
