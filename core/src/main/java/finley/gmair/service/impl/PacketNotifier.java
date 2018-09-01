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

    @Value("${machine_v1_queue_name}")
    private String machineV1QueueName;

    @Value("${queue_name}")
    private String queueName;

    @Value("${partial_data_queue_name}")
    private String partialDataQueueName;

    @Autowired
    AmqpTemplate template;

    public ResultData sendV1(String uid) {
        ResultData result = new ResultData();
        this.template.convertAndSend(machineV1QueueName, uid);
        return result;
    }

    public ResultData send(String uid) {
        ResultData result = new ResultData();
        this.template.convertAndSend(queueName, uid);
        return result;
    }

    public ResultData sendPartialData(String uid) {
        ResultData result = new ResultData();
        this.template.convertAndSend(partialDataQueueName, uid);
        return result;
    }
}
