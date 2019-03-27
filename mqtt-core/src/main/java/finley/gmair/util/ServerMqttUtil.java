package finley.gmair.util;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class ServerMqttUtil {
    private static final String HOST = "tcp://116.62.233.170:61613";

    private static final String clientId = "core-server";

    private MqttClient client;
    private MqttTopic mqttTopic;

    /**
     * ServerMqttUtil constructor
     * @param topic
     * @throws MqttException
     * */
    public ServerMqttUtil(String[] topic) throws MqttException {
        client = new MqttClient(HOST, clientId, new MemoryPersistence());
        connect(topic);
    }

    /**
     * called to connect broker
     * subscribe topic
     * @param topic
     * */
    private void connect(String[] topic) {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(false);
        options.setKeepAliveInterval(20);
        options.setConnectionTimeout(10);
        try {
            client.setCallback(new PushCallback());
            //mqttTopic = client.getTopic(topic);
            //options.setWill(mqttTopic, "close".getBytes(), 2, true);
            client.connect(options);
            int[] Qos = {1};
            client.subscribe(topic, Qos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * send message and get callback
     * @param
     * */
    public void publish(String topic, MqttMessage message) throws MqttPersistenceException,
            MqttException, InterruptedException {
        mqttTopic = client.getTopic(topic);
        MqttDeliveryToken token = mqttTopic.publish(message);
        token.waitForCompletion();
        System.out.println("message is published completely! "
                + token.isComplete());
        System.out.println("messageId:" + token.getMessageId());
        token.getResponse();
        if (client.isConnected())
            client.disconnect(10000);
        System.out.println("Disconnected: delivery token \"" + token.hashCode()
                        + "\" received: " + token.isComplete());
    }
}
