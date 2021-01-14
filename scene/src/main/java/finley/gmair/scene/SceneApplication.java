package finley.gmair.scene;

import com.maihaoche.starter.mq.annotation.EnableMQConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@EnableMQConfiguration
@MapperScan("finley.gmair.scene.dao.repository")
public class SceneApplication {

    public static void main(String[] args) {
        SpringApplication.run(SceneApplication.class, args);
    }

}
