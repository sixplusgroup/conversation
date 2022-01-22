package finley.gmair.config;

import finley.gmair.util.JingdongApiHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JingdongApiConfig {
    @Bean
    public JingdongApiHelper jingdongApiHelper(JingDongProperties properties) {
        return new JingdongApiHelper(properties);
    }
}
