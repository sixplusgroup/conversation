package finley.gmair.scene.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author : Lyy
 * @create : 2021-01-15 11:11
 * @description 极光推送配置信息
 **/
@Data
@Configuration
public class JPushConfig {
    // 读取极光配置信息中的用户名密码
    @Value("${jpush.appKey}")
    private String appkey;
    @Value("${jpush.masterSecret}")
    private String masterSecret;
    @Value("${jpush.liveTime}")
    private String liveTime;
}
