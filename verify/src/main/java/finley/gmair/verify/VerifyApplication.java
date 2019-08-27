package finley.gmair.verify;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Author ：lycheeshell
 * @Date ：Created in 15:04 2019/8/27
 */
@ComponentScan({"finley.gmair.service", "finley.gmair.controller"})
@EnableEurekaClient
@SpringBootApplication
public class VerifyApplication {

    public static void main(String[] args) {
        SpringApplication.run(VerifyApplication.class, args);
    }

}
