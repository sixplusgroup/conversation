package finley.gmair.reception;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableFeignClients({"finley.gmair.service"})
@EnableDiscoveryClient
@ComponentScan({"finley.gmair.service", "finley.gmair.controller"})
public class ReceptionApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReceptionApplication.class, args);
    }
}
