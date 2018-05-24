package finley.gmair.machine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@ComponentScan({"finley.gmair.service", "finley.gmair.dao", "finley.gmair.controller", "finley.gmair.config"})
@SpringBootApplication
@EnableScheduling
@EnableCaching
public class MachineApplication {
    public static void main(String[] args) {
        SpringApplication.run(MachineApplication.class, args);
    }
}
