package finley.gmair.mqtt;

import lombok.Getter;
import lombok.Setter;

/**
 * mqtt订阅配置类
 *
 * @author liuzongkan
 * @date 2020/12/3 15:35
 */
@Getter
@Setter
public class MqttInbound {

    /**
     * mq服务器url
     */
    private String url;

    /**
     * clientId
     */
    private String clientId;

}
