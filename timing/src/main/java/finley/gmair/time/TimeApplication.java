package finley.gmair.time;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"finley.gmair.config", "finley.gmair.job"})
@EnableFeignClients({"finley.gmair.service"})
public class TimeApplication {

    public static void main(String[] args) {
        SpringApplication.run(TimeApplication.class, args);
    }
}
