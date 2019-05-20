package finley.gmair.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @ClassName: InstallDeskApplication
 * @Description: TODO
 * @Author fan
 * @Date 2019/3/19 5:18 PM

@SpringBootApplication
@EnableFeignClients(basePackages = "finley.gmair.service")
@ComponentScan({"finley.gmair.controller", "finley.gmair.service", "finley.gmair.dao"})
public class InstallDeskApplication {
    public static void main(String[] args) {
        SpringApplication.run(InstallDeskApplication.class, args);
    }
}
