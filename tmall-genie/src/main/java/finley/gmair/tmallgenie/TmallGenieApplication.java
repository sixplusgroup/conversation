package finley.gmair.tmallgenie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients({"finley.gmair.service"})
@ComponentScan({"finley.gmair.controller", "finley.gmair.service", "finley.gmair.config"})
public class TmallGenieApplication {
    public static void main(String[] args) {
        SpringApplication.run(TmallGenieApplication.class, args);
    }
}
