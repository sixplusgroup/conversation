package finley.gmair.express;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@ComponentScan({"finley.gmair.controller", "finley.gmair.service", "finley.gmair.dao", "finley.gmair.Schedule", "finley.gmair.company"})
@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
public class ExpressApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExpressApplication.class, args);
    }
}
