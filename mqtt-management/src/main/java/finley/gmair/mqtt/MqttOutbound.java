package finley.gmair.mqtt;

import lombok.Getter;
import lombok.Setter;

/**
 * mqtt发布配置类
 *
 * @author liuzongkan
 * @date 2020/12/3 15:35
 */
@Getter
@Setter
public class MqttOutbound {

    /**
     * mq服务器url
     */
    private String urls;

    /**
     * clientId
     */
    private String clientId;

    /**
     * 默认主题
     */
    private String topic;
}
