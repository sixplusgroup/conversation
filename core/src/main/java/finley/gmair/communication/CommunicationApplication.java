package finley.gmair.communication;

import finley.gmair.netty.GMServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({ "finley.gmair.netty", "finley.gmair.handler", "finley.gmair.controller"})
@SpringBootApplication
public class CommunicationApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(CommunicationApplication.class, args);
        GMServer server = context.getBean(GMServer.class);
        server.start();
    }
}
