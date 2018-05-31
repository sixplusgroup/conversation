package finley.gmair.management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"finley.gmair.config", "finley.gmair.sf.config", "finley.gmair.controller", "finley.gmair.service"})
@EnableFeignClients(basePackages = "finley.gmair.service")
public class ManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(ManagementApplication.class, args);
    }
}
