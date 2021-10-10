package finley.gmair.config;

import lombok.Data;

/**
 * @Author Joby
 */
@Data
public class PayConfig {
    private String appId;
    private String merchantId;
    private String key;
    private String notifyUrl;
}