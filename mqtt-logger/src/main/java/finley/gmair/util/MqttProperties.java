package finley.gmair.util;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.mqtt")
public class MqttProperties {
    private MqttInbound inbound;

    public MqttInbound getInbound() {
        return inbound;
    }

    public void setInbound(MqttInbound inbound) {
        this.inbound = inbound;
    }

}
