package finley.gmair;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({"finley.gmair.controller", "finley.gmair.service", "finley.gmair.dao", "finley.gmair.config"})
@EnableCaching
@EnableEurekaClient
@EnableFeignClients(basePackages = "finley.gmair.service")
@EnableDiscoveryClient
@SpringBootApplication
public class DataAnalysisApplication {
    public static void main(String[] args) {
        SpringApplication.run(DataAnalysisApplication.class, args);
    }
}
