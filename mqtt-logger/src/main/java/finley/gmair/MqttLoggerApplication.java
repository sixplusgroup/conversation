package finley.gmair;

import finley.gmair.util.MqttProperties;
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
//@EnableMongoRepositories(basePackages = "finley.gmair.repo")
//@EnableFeignClients({"finley.gmair.LoggerRecordService"})
@EnableEurekaClient
public class MqttLoggerApplication {
    public static void main(String[] args) {
        SpringApplication.run(MqttLoggerApplication.class, args);
    }
}
