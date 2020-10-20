package finley.gmair;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan({"finley.gmair.controller", "finley.gmair.service", "finley.gmair.dao", "finley.gmair.schedule"})
@MapperScan("finley.gmair.dao")
@EnableFeignClients(basePackages = "finley.gmair.service")
@EnableScheduling
public class OrderNewApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderNewApplication.class, args);
    }
}
