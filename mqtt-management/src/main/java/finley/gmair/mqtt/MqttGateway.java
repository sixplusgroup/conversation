package finley.gmair.mqtt;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

/**
 * 定义重载方法，用于消息发送
 *
 * @author lycheeshell
 * @date 2020/12/15 17:26
 */
@Component
@MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
public interface MqttGateway {

    /**
     * 向默认主题发送消息
     *
     * @param payload 发送的内容
     */
    void sendToMqtt(String payload);

    /**
     * 向指定主题发送消息
     *
     * @param topic   主题
     * @param payload 发送的内容
     */
    void sendToMqtt(@Header(MqttHeaders.TOPIC) String topic, String payload);

    /**
     * 向指定主题发送消息，并指定服务质量
     *
     * @param topic   主题
     * @param qos     服务质量
     * @param payload 发送的内容
     */
    void sendToMqtt(@Header(MqttHeaders.TOPIC) String topic, @Header(MqttHeaders.QOS) int qos, String payload);
}
