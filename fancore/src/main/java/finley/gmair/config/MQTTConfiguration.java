package finley.gmair.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import finley.gmair.pool.CorePool;
import finley.gmair.service.LogService;
import finley.gmair.service.MqttService;
import finley.gmair.service.RedisService;
import finley.gmair.service.TopicService;
import finley.gmair.util.MQTTUtil;
import finley.gmair.util.MqttProperties;
import finley.gmair.util.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessageHeaders;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@PropertySource({"classpath:auth.properties", "classpath:mqtt.properties"})
@Configuration
public class MqttConfiguration {
    private Logger logger = LoggerFactory.getLogger(MqttConfiguration.class);

    @Autowired
    private MqttProperties mqttProperties;

    @Autowired
    private TopicService topicService;

    @Autowired
    private LogService logService;

    @Autowired
    private MqttService mqttService;

    @Value("${inbound_url}")
    private String ip;

    @Value("${username}")
    private String username;

    @Value("${password}")
    private String password;

    @Value("${replica}")
    private boolean isReplica;

    @Autowired
    private RedisService redisService;

    private Map<String, Integer> devices = new ConcurrentHashMap<>();

    /**
     * 实现MqttOutbound
     */
    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        String[] serverUrls = mqttProperties.getOutbound().getUrls().split(",");
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        //factory.setUserName(username);
        //factory.setPassword(password);
        factory.setServerURIs(serverUrls);
        factory.setCleanSession(true);
        factory.setConnectionTimeout(10);
        factory.setKeepAliveInterval(20);
        return factory;
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutbound() {
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(mqttProperties.getOutbound().getClientId(), mqttClientFactory());
        messageHandler.setAsync(true);
        //messageHandler.setDefaultTopic(mqttProperties.getOutbound().getTopic());
        return messageHandler;
    }

    @Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }


    /**
     * 实现MqttInbound
     */
    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageProducer inbound() {
        String[] inboundTopic = topicDetail();
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(mqttProperties.getInbound().getClientId(), mqttClientFactory(), inboundTopic);
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler() {
        return new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) {
                CorePool.getHandlePool().execute(() -> {
                    if (message == null) {
                        logger.error("[Error] illegal message received.");
                        return;
                    }
                    MessageHeaders headers = message.getHeaders();
                    if (headers == null) return;
                    String topic = headers.get("mqtt_topic").toString();
                    //判断是否要丢弃报文
                    if (TimeUtil.exceed(headers.getTimestamp(), System.currentTimeMillis(), 300)) {
                        logger.info("Timestamp of the received package elapsed the duration, thus the package is aborted.");
                        return;
                    }
                    String payload = ((String) message.getPayload());
                    //将payload转换为json数据格式，进行进一步处理
                    if (headers.containsKey("mqtt_duplicate") && (Boolean) headers.get("mqtt_duplicate") == true)
                        return;
                    logger.info("topic: " + topic + ", message: " + JSON.toJSONString(payload));
                    handle(topic, JSON.parseObject(payload));
                });
            }
        };
    }

    /**
     * 获取当前需订阅的所有topic
     */
    private String[] topicDetail() {
        String[] topics = new String[]{"/GM/FAN/+/INFO", "/GM/FAN/+/STATUS", "/GM/FAN/+/WARN", "/GM/FAN/+/RESPONSE", "/GM/FAN/+/TIMING", "/GM/FAN/TIMING"};
        return topics;
    }

    /**
     * 处理当前message的信息
     */
    private void handle(String topic, JSONObject json) {
        try {
            //判断是否处理该报文
            if (topic.startsWith("/")) topic = topic.substring(1);
            //将topic根据"/"切分为string数组，方便处理
            //判断是否为时钟同步的消息
            String[] array = topic.split("/");
            //根据定义的topic格式，获取message对应的machineId
            if (array.length != 4) {
                logger.info("Unrecognized topic: " + topic);
                return;
            }
            String model = array[2], action = array[3];
            if (!json.containsKey("timestamp")) return;
            long t = json.getLong("timestamp");
            if (TimeUtil.exceed(t, System.currentTimeMillis(), 300)) {
                logger.info("当前时间戳无效，需要同步");
                MQTTUtil.publishTimeSyncTopic(mqttService, model, json.getString("mac"));
                return;
            }
            String mac = "";
            switch (action.toUpperCase()) {
                case "INFO":
                    mac = json.containsKey("mac") ? json.getString("mac") : mac;
                    break;
                case "TIMING":
                    if (json.containsKey("mac")) {
                        mac = json.getString("mac");
                        MQTTUtil.publishTimeSyncTopic(mqttService, model, mac);
                    }
                    break;
                case "STATUS":
                    logger.info("Status info: " + JSON.toJSONString(json));
                    break;
                case "WARN":
                    logger.info("Warn info: " + JSON.toJSONString(json));
                    break;
                case "RESPONSE":
                    logger.info("Action info: " + JSON.toJSONString(json));
                default:
                    logger.info("Unrecognized action: " + action);
            }
        } catch (Exception e) {
//            logger.error("[Error ] " + e.getMessage());
        }
    }
}
