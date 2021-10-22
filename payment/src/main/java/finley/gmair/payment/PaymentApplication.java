package finley.gmair.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @ClassName: PaymentApplication
 * @Description: TODO
 * @Author fan
 * @Date 2019/7/23 3:03 PM
 */

@ComponentScan({"finley.gmair.service", "finley.gmair.dao", "finley.gmair.controller","finley.gmair.config"})
@EnableDiscoveryClient
@EnableFeignClients({"finley.gmair.service"})
@SpringBootApplication
public class PaymentApplication {
    public static void main(String[] args) {
        SpringApplication.run(PaymentApplication.class, args);
    }
}
