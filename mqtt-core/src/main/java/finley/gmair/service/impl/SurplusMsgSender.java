package finley.gmair.service.impl;

import finley.gmair.model.mqtt.SurplusPayload;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author: Bright Chan
 * @date: 2020/7/29 18:32
 * @description: TODO
 */

@Component
@PropertySource("classpath:rabbitmq.properties")
public class SurplusMsgSender {

    @Value("${surplus_queue_name}")
    private String surplusQueue;

    @Autowired
    AmqpTemplate amqpTemplate;

    public void sendSurplus(SurplusPayload surplusPayload) {
        amqpTemplate.convertAndSend(surplusQueue, surplusPayload);
    }
}
