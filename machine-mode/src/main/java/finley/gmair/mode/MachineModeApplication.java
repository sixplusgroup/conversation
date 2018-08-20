package finley.gmair.mode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@ComponentScan({"finley.gmair.service", "finley.gmair.dao", "finley.gmair.controller", "finley.gmair.agent"})
@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
@EnableFeignClients({"finley.gmair.service","finley.gmair.agent"})
public class MachineModeApplication {
    public static void main(String[] args) {
        SpringApplication.run(MachineModeApplication.class, args);
    }
}
