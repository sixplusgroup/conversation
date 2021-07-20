package finley.gmair.scene.mq;

import com.maihaoche.starter.mq.annotation.MQProducer;
import com.maihaoche.starter.mq.base.AbstractMQProducer;

import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

/**
 * @author : Lyy
 * @create : 2021-01-14 16:47
 **/
@MQProducer
public class SceneOperationProducer extends AbstractMQProducer {

    @Override
    public void doAfterSyncSend(Message message, SendResult sendResult) {
        super.doAfterSyncSend(message, sendResult);
    }

}
