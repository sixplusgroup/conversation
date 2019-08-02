package finley.gmair;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableFeignClients({"finley.gmair.service"})
public class DriftApplication {

    public static void main(String[] args) {
        SpringApplication.run(DriftApplication.class, args);
    }
}
