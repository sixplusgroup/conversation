package finley.gmair.config;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author Joby
 */
@Data
@AllArgsConstructor
public class PayConfig {
    private String appId;
    private String merchantId;
    private String key;
    private String notifyUrl;
    public PayConfig(){

    }
}