package finley.gmair;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@ComponentScan({"finley.gmair.service", "finley.gmair.dao", "finley.gmair.config"})
@MapperScan({"finley.gmair.dao"})
@EnableScheduling
@EnableCaching
@EnableEurekaClient
@EnableFeignClients(basePackages = "finley.gmair.service")
@EnableDiscoveryClient
@SpringBootApplication
public class ChatlogQueryApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChatlogQueryApplication.class, args);
    }
}
