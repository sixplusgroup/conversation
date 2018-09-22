package finley.gmair.drift;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"finley.gmair.controller", "finley.gmair.service", "finley.gmair.dao"})
public class DriftApplication {

    public static void main(String[] args) {
        SpringApplication.run(DriftApplication.class, args);
    }
}
