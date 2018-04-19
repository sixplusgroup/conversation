package finley.gmair.express;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({"finley.gmair.controller", "finley.gmair.service", "finley.gmair.dao"})
@SpringBootApplication
@EnableDiscoveryClient
public class ExpressApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExpressApplication.class, args);
    }
}
