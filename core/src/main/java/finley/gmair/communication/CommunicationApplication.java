package finley.gmair.communication;

import finley.gmair.netty.GMServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@ComponentScan({ "finley.gmair.netty", "finley.gmair.handler", "finley.gmair.controller", "finley.gmair.service", "finley.gmair.dao"})
@SpringBootApplication
@EnableMongoRepositories(basePackages = "finley.gmair.repo")
@EnableFeignClients(basePackages = "finley.gmair.service")
@EnableCaching
public class CommunicationApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(CommunicationApplication.class, args);
        GMServer server = context.getBean(GMServer.class);
        server.start();
    }
}
