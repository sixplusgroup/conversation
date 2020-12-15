package finley.gmair.mqtt;

import lombok.Data;

/**
 * mqtt发布配置类
 *
 * @author liuzongkan
 * @date 2020/12/3 15:35
 */
@Data
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
