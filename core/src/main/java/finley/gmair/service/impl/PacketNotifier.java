package finley.gmair.service.impl;

import finley.gmair.util.ResultData;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:rabbitmq.properties")
public class PacketNotifier {
    @Value("${queue_name}")
    private String queueName;

    @Autowired
    AmqpTemplate template;

    public ResultData send(String uid) {
        ResultData result = new ResultData();
        this.template.convertAndSend(queueName, uid);
        return result;
    }
}
