package finley.gmair.assemble;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({"finley.gmair.service", "finley.gmair.dao", "finley.gmair.controller"})
@EnableDiscoveryClient
@EnableFeignClients({"finley.gmair.service"})
@SpringBootApplication
public class AssembleApplication {
    public static void main(String[] args) {
        SpringApplication.run(AssembleApplication.class, args);
    }
}
