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
     * 连接的用户名
     */
    private String username;

    /**
     * 连接的密码
     */
    private String password;

    /**
     * clientId
     */
    private String clientId;
}
