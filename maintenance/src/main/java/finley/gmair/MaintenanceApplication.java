package finley.gmair;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

/**
 * 设备检修辅助系统启动类
 *
 * @author lycheeshell
 * @date 2021/1/17 14:32
 */
@SpringBootApplication
@EnableFeignClients({"finley.gmair.service"})
@EnableEurekaClient
public class MaintenanceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MaintenanceApplication.class, args);
    }

}
