package finley.gmair.backdoor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({"finley.gmair.schedule", "finley.gmair.service"})
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients({"finley.gmair.service"})
public class BackdoorApplication {
    public BackdoorApplication() {
    }

    public static void main(String[] args) {
        SpringApplication.run(BackdoorApplication.class, args);
    }
}
