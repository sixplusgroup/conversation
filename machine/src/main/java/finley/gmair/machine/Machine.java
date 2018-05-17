package finley.gmair.machine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@ComponentScan({"finley.gmair.service", "finley.gmair.dao", "finley.gmair.controller", "finley.gmair.config"})
@SpringBootApplication
@EnableScheduling
public class Machine {
    public static void main(String[] args) {
        SpringApplication.run(Machine.class, args);
    }
}
