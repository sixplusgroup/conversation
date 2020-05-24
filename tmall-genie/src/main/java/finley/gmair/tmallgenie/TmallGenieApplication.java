package finley.gmair.tmallgenie;

import finley.gmair.service.MachineService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients({"finley.gmair.service"})
@ComponentScan({"finley.gmair.controller", "finley.gmair.service"})
public class TmallGenieApplication {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(TmallGenieApplication.class, args);
        System.out.println(context.getBean(MachineService.class));
    }
}
