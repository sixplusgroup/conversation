package finley.gmair;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@EnableFeignClients(basePackages = "finley.gmair.service")
@EnableScheduling
@EnableCaching
public class AirQualityApplication {
    public static void main(String[] args) {
        SpringApplication.run(AirQualityApplication.class, args);
    }
}