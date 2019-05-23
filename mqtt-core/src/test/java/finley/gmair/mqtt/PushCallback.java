package finley.gmair.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName: PushCallback
 * @Description: TODO
 * @Author fan
 * @Date 2019/3/20 2:25 PM
 */
public class PushCallback implements MqttCallback {
    private Logger logger = LoggerFactory.getLogger(PushCallback.class);

    public void connectionLost(Throwable cause) {
        // 连接丢失后，一般在这里面进行重连
        logger.info("连接断开，可以做重连");
    }

    public void deliveryComplete(IMqttDeliveryToken token) {
        logger.info("deliveryComplete---------" + token.isComplete());
    }

    public void messageArrived(String topic, MqttMessage message) throws Exception {
        // subscribe后得到的消息会执行到这里面
        logger.info("接收消息主题 : " + topic);
        logger.info("接收消息Qos : " + message.getQos());
        logger.info("接收消息内容 : " + new String(message.getPayload()));
    }
}
