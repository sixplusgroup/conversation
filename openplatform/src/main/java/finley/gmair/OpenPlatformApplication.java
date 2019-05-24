package finley.gmair;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @ClassName: OpenPlatformApplication
 * @Description: TODO
 * @Author fan
 * @Date 2019/5/7 11:12 AM
 */
@SpringBootApplication
@EnableFeignClients(basePackages = "finley.gmair.service")
public class OpenPlatformApplication {
    public static void main(String[] args) {
        SpringApplication.run(OpenPlatformApplication.class, args);
    }
}
