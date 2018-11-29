package finley.gmair.service.impl;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:rabbitmq.properties")
public class ActionNotifier {

    @Value("${turn_on_queue_name}")
    private String turnOnQueue;

    @Value("${turn_off_queue_name}")
    private String turnOffQueue;

    @Autowired
    AmqpTemplate amqpTemplate;

    public void sendTurnOn(String uid) {
        this.amqpTemplate.convertAndSend(turnOnQueue, uid);
    }

    public void sendTurnOff(String uid) {
        this.amqpTemplate.convertAndSend(turnOffQueue, uid);
    }
}
