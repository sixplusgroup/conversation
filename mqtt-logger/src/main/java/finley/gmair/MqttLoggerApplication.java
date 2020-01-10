package finley.gmair;

import finley.gmair.util.MqttProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Component;

/**
 * @author zjd123
 * @date 2020/1/4 - 10:53
 */
@SpringBootApplication
@Component
@Configuration
@EnableConfigurationProperties(MqttProperties.class)
@EnableEurekaClient
public class MqttLoggerApplication {
    private final static Logger logger = LoggerFactory.getLogger(MqttLoggerApplication.class);

    public static void main(String[] args) {
        logger.info("[Info] boot mqtt logger node...");
        SpringApplication.run(MqttLoggerApplication.class, args);
        logger.info("[Info] mqtt logger has been started successfully");
    }
}
