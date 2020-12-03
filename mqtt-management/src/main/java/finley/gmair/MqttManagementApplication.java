package finley.gmair;

import finley.gmair.mqtt.MqttProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Component;

/**
 * 消息推送系统启动类
 *
 * @author lycheeshell
 * @date 2020/12/3 15:04
 */
@SpringBootApplication
@Component
@Configuration
@EnableConfigurationProperties(MqttProperties.class)
@EnableMongoRepositories(basePackages = "finley.gmair.repo")
@EnableFeignClients({"finley.gmair.service"})
@EnableEurekaClient
public class MqttManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(MqttManagementApplication.class, args);
    }

}
