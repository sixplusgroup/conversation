package finley.gmair.preparation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@ComponentScan({"finley.gmair.controller", "finley.gmair.service"})
@RestController
@RequestMapping("/preparation")
@EnableDiscoveryClient
@EnableFeignClients("finley.gmair.service")
public class PreparationApplication {
    public static void main(String[] args) {
        SpringApplication.run(PreparationApplication.class, args);
    }
}
