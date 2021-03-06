package finley.gmair;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
//import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
//import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

//@EnableScheduling
//@EnableCaching
//@EnableEurekaClient
//@EnableFeignClients(basePackages = "finley.gmair.service")
//@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan(basePackages = {"finley.gmair.*"})
public class ChatlogAnalysisApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChatlogAnalysisApplication.class, args);
    }
}
