package finley.gmair.mqtt;

import finley.gmair.util.MqttProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Component;

@SpringBootApplication
@Component
@Configuration
@EnableConfigurationProperties(MqttProperties.class)
@EnableMongoRepositories(basePackages = "finley.gmair.repo")
@EnableFeignClients({"finley.gmair.service"})
@EnableEurekaClient
@ComponentScan({"finley.gmair.config", "finley.gmair.util", "finley.gmair.dao", "finley.gmair.service", "finley.gmair.controller"})
public class MqttApplication {
    public static void main(String[] args) {
        SpringApplication.run(MqttApplication.class, args);
    }
}
