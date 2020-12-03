package finley.gmair.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author lycheeshell
 * @date 2020/12/3 15:23
 */
@PropertySource({"classpath:auth.properties", "classpath:mqtt.properties"})
@Configuration
public class MqttConfiguration {
}
