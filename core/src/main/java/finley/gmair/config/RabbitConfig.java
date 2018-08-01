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

    @Value("${partial_data_queue_name}")
    private String partialDataQueueName;

    @Bean
    public Queue machineQueue() {
        return new Queue(queueName);
    }

    @Bean
    public Queue partialDataQueue() {
        return new Queue(partialDataQueueName);
    }


}
