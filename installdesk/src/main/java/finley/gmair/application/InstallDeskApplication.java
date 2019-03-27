package finley.gmair.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author fan
 */
@SpringBootApplication
@ComponentScan({"finley.gmair.controller", "finley.gmair.service", "finley.gmair.dao"})
public class InstallDeskApplication {
    public static void main(String[] args) {
        SpringApplication.run(InstallDeskApplication.class, args);
    }
}
