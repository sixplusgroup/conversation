package finley.gmair.mode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@ComponentScan({"finley.gmair.service", "finley.gmair.dao", "finley.gmair.controller", "finley.gmair.config"})
@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
@EnableCaching
@EnableFeignClients({"finley.gmair.service"})
public class ModeApplication {
    public static void main(String[] args) {
        SpringApplication.run(ModeApplication.class, args);
    }
}
