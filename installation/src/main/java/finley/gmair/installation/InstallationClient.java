package finley.gmair.installation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@ComponentScan({"finley.gmair.controller", "finley.gmair.service", "finley.gmair.dao","finley.gmair.scheduler.installation"})
@RestController
@EnableFeignClients(basePackages = "finley.gmair.service")
@RequestMapping("/installation")
@EnableDiscoveryClient
@EnableScheduling
public class InstallationClient {
    public static void main(String... args) {
        SpringApplication.run(InstallationClient.class, args);
    }
}
