package finley.gmair;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @ClassName: MembershipApplication
 * @Description: TODO
 * @Author fan
 * @Date 2021/7/12 11:44 PM
 */

@SpringBootApplication
@ComponentScan({"finley.gmair.dao", "finley.gmair.service", "finley.gmair.controller",  "finley.gmair.config"})
@EnableDiscoveryClient
@EnableTransactionManagement //also can not add this annotation
@EnableFeignClients
public class MembershipApplication {
    public static void main(String[] args) {
        SpringApplication.run(MembershipApplication.class, args);
    }
}
