package finley.gmair.service.impl;


import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "machine-queue")
public class MachineReceiver {

    @RabbitHandler
    public void process(String uid) {
        System.out.println("uid");
    }
}
