package finley.gmair.config;


import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:rabbitmq.properties")
public class RabbitMQConfig {

    @Value("${surplus_queue_name}")
    private String surplusQueue;

    @Bean
    public Queue queue() {
        return new Queue(surplusQueue);
    }
}
