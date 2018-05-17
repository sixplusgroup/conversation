package finley.gmair.log;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({ "finley.gmair.controller", "finley.gmair.service", "finley.gmair.dao"})
@SpringBootApplication
public class LogApplication {
    public static void main(String[] args) {
        SpringApplication.run(LogApplication.class, args);
    }
}
