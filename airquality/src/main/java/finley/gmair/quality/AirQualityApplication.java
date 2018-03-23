package finley.gmair.quality;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@ComponentScan({"finley.gmair.service", "finley.gmair.dao"})
public class AirQualityApplication {
    public static void main(String[] args) {
        SpringApplication.run(AirQualityApplication.class, args);
    }
}
