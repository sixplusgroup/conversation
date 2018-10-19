package finley.gmair.formaldehyde;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@EnableFeignClients({"finley.gmair.service"})
@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan({"finley.gmair.service", "finley.gmair.controller", "finley.gmair.dao"})
public class FormaldehydeApplication {
    public static void main(String[] args) {
        SpringApplication.run(FormaldehydeApplication.class, args);
    }
}
