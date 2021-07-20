package finley.gmair;

import finley.gmair.util.MqttProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * @ClassName: FanApplication
 * @Description: TODO
 * @Author fan
 * @Date 2019/11/12 5:17 PM
 */
@EnableConfigurationProperties(MqttProperties.class)
@EnableFeignClients({"finley.gmair.service"})
@SpringBootApplication
public class FanApplication {
    private static Logger logger = LoggerFactory.getLogger(FanApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(FanApplication.class, args);
        logger.info("[Info] Application started.");
    }
}
