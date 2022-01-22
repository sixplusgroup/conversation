package finley.gmair.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "api.jingdong")
public class JingDongProperties {
    private String serverUrl;
    private String accessToken;
    private String appKey;
    private String appSecret;
}
