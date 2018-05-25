package finley.gmair.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath: rabbitmq.properties")
public class RabbitConfig {

    @Value("${queue_name}")
    private String queueName;

    @Bean
    public Queue machineQueue() {
        return new Queue(queueName);
    }
}
