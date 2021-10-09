package finley.gmair.config;

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
public class PayConfig {

    private Oa oa;// official account

    private Mp mp;// mini program


}
