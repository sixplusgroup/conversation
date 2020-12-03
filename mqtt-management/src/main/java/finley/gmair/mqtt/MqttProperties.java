package finley.gmair.mqtt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * mqtt配置类
 *
 * @author liuzongkan
 * @date 2020/12/3 15:39
 */
@ConfigurationProperties(prefix = "spring.mqtt")
@Data
public class MqttProperties {

    /**
     * 订阅配置
     */
    private MqttInbound inbound;

    /**
     * 发布配置
     */
    private MqttOutbound outbound;
}
