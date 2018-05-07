package finley.gmair.install;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"finley.gmair.controller", "finley.gmair.service", "finley.gmair.dao"})
@EnableDiscoveryClient
public class InstallMPApplication {
    public static void main(String[] args) {
        SpringApplication.run(InstallMPApplication.class, args);
    }
}

