package finley.gmair;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"finley.gmair.controller", "finley.gmair.service", "finley.gmair.dao",
        "finley.gmair.repo", "finley.gmair.handler", "finley.gmair.converter", "finley.gmair.event"})
@MapperScan("finley.gmair.dao")
@EnableFeignClients(basePackages = "finley.gmair.service")
public class OrderCentreApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderCentreApplication.class, args);
    }
}
