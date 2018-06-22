package finley.gmair.machine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@ComponentScan({"finley.gmair.service", "finley.gmair.dao", "finley.gmair.controller", "finley.gmair.config"})
@SpringBootApplication
@EnableScheduling
@EnableCaching
@EnableFeignClients({"finley.gmair.service"})
@EnableMongoRepositories(basePackages = "finley.gmair.repo")
public class MachineApplication {
    public static void main(String[] args) {
        SpringApplication.run(MachineApplication.class, args);
    }
}
