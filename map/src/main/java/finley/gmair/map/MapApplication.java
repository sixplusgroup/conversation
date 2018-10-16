package finley.gmair.map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({"finley.gmair.service", "finley.gmair.controller"})
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients({"finley.gmair.service"})
public class MapApplication {
    public static void main(String[] args) {
        SpringApplication.run(MapApplication.class, args);
    }

}
