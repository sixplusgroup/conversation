package finley.gmair.drift;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableFeignClients({"finley.gmair.service"})
@ComponentScan({"finley.gmair.controller", "finley.gmair.service", "finley.gmair.dao"})
public class DriftApplication {

    public static void main(String[] args) {
        SpringApplication.run(DriftApplication.class, args);
    }
}
