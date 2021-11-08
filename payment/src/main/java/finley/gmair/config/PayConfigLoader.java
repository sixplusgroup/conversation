package finley.gmair.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @Author Joby
 */
@Component
@Data
@PropertySource("classpath:wechatpay.properties")
@ConfigurationProperties(prefix = "pay")
public class PayConfigLoader {

    private PayConfig oa;// official account

    private PayConfig mp;// mini program

}
