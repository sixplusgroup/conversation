package finley.gmair.mqtt;

import lombok.Data;

/**
 * mqtt订阅配置类
 *
 * @author liuzongkan
 * @date 2020/12/3 15:35
 */
@Data
public class MqttInbound {

    /**
     * mq服务器url
     */
    private String url;

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
