package finley.gmair.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import finley.gmair.service.MqttService;
import finley.gmair.util.MqttProperties;
import finley.gmair.util.PushCallback;
import finley.gmair.util.ResultData;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName: MqttServiceImpl
 * @Description: TODO
 * @Author fan
 * @Date 2019/5/11 6:10 PM
 */
@Service
public class MqttServiceImpl implements MqttService {
    private Logger logger = LoggerFactory.getLogger(MqttServiceImpl.class);

    @Autowired
    private MqttProperties properties;

    private MqttClient client;

    private Map<String, JSONObject> status = new ConcurrentHashMap<>();

    public MqttServiceImpl() {
        init();
    }

    public void refresh(String mac, JSONObject o) {
        status.put(mac, o);
        logger.info("Current stored information device: " + status.size());
    }

    @Override
    public JSONObject obtain(String mac) {
        return status.get(mac);
    }

    @Override
    public ResultData publish(String topic, JSONObject object) {
        ResultData result = new ResultData();
        if (client == null) init();
        if (!client.isConnected()) {
            try {
                MqttConnectOptions options = new MqttConnectOptions();
                options.setCleanSession(false);
                options.setKeepAliveInterval(30);
                options.setConnectionTimeout(10);
                client.setCallback(new PushCallback());
                client.connect(options);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
        MqttMessage message = getMessage(object);
        try {
            client.publish(topic, message);
        } catch (Exception e) {
            logger.error("[Error] publish topic failure " + e.getMessage());

        }
        return result;
    }

    private void init() {
        logger.info("[Info] prepare a new client");
        try {
            client = new MqttClient(properties.getOutbound().getUrls(), properties.getOutbound().getClientId(), new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(false);
            options.setKeepAliveInterval(30);
            options.setConnectionTimeout(10);
            client.setCallback(new PushCallback());
            client.connect(options);
            logger.info("[Info] MQTT client initialize successfully");
        } catch (Exception e) {
            logger.error("[Error]" + e.getMessage());
        }
    }

    /**
     * 根据json数据build相应的MqttMessage
     */
    private MqttMessage getMessage(JSONObject object) {
        MqttMessage message = new MqttMessage();
        message.setQos(0);
        message.setRetained(false);
        message.setPayload(object.toString().getBytes());
        return message;
    }
}
