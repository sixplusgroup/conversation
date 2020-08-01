package finley.gmair.service.impl;

import finley.gmair.model.mqtt.SurplusPayload;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author: Bright Chan
 * @date: 2020/7/29 18:47
 * @description: TODO
 */

@Component
@RabbitListener(queues = "surplus-queue")
public class SurplusMsgReceiver {

    private SurplusPayload surplusPayload = null;

    @RabbitHandler
    public void process(SurplusPayload surplusPayload) {
        System.out.println("machineId: " + surplusPayload.getMachineId() + " remain: " + surplusPayload.getRemain());
        this.surplusPayload = surplusPayload;
    }

    public SurplusPayload getLatestSurplus() {
        return this.surplusPayload;
    }
}
