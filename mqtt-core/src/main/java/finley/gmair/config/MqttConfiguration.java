package finley.gmair.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import finley.gmair.controller.MachineController;
import finley.gmair.controller.MessageController;
import finley.gmair.datastructrue.LimitQueue;
import finley.gmair.model.machine.MachineStatusV3;
import finley.gmair.model.mqtt.AckPayload;
import finley.gmair.model.mqtt.AlertPayload;
import finley.gmair.model.mqtt.SurplusPayload;
import finley.gmair.model.mqtt.Topic;
import finley.gmair.pool.CorePool;
import finley.gmair.repo.MachineStatusV3Repository;
import finley.gmair.service.LogService;
import finley.gmair.service.MqttService;
import finley.gmair.service.RedisService;
import finley.gmair.service.TopicService;
import finley.gmair.util.*;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
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
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@PropertySource({"classpath:auth.properties", "classpath:mqtt.properties"})
@Configuration
public class MqttConfiguration implements MqttCallback {
    private Logger logger = LoggerFactory.getLogger(MqttConfiguration.class);

    @Autowired
    private MqttProperties mqttProperties;

    @Autowired
    private TopicService topicService;

    @Autowired
    private MachineController machineController;

    @Autowired
    private LogService logService;

    @Autowired
    private MqttService mqttService;

    @Autowired
    private MessageController messageController;

    private MqttClient client;

    private static final int[] qos = {2};

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

    @Autowired
    private MachineStatusV3Repository repository;

    private Map<String, Integer> devices = new ConcurrentHashMap<>();

    public MqttConfiguration() {
        init();
    }

    private void init() {
        logger.info("[Info] prepare a new client");
        try {
            client = new MqttClient(mqttProperties.getOutbound().getUrls(), mqttProperties.getOutbound().getClientId(), new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            options.setKeepAliveInterval(30);
            options.setConnectionTimeout(10);
            client.setCallback(this);
            client.connect(options);
            logger.info("[Info] MQTT client initialize successfully");
        } catch (Exception e) {
            logger.error("[Error]" + e.getMessage());
        }
    }

    /**
     *
     * 订阅topic
     * */
    @Bean
    public MqttClient subscribe() {
        if (client == null) init();
        if (!client.isConnected()) {
            try {
                MqttConnectOptions options = new MqttConnectOptions();
                options.setCleanSession(true);
                options.setKeepAliveInterval(30);
                options.setConnectionTimeout(10);
                client.setCallback(this);
                client.connect(options);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
        try {
            client.subscribe(topicDetail(), qos);
        } catch (Exception e) {
            logger.error("[Error] subscribe topics failure " + e.getMessage());
        }
        return client;
    }

    public void messageArrived(String topic, MqttMessage message) throws MqttException {
        CorePool.getHandlePool().execute(() -> {
            if (message == null) {
                logger.error("[Error] illegal message received.");
                return;
            }
            JSONObject json = JSON.parseObject(new String(message.getPayload(), StandardCharsets.UTF_8));
            //判断是否要丢弃报文
            if (json.containsKey("time")) {
                if (TimeUtil.exceed(json.getLong("time") * 1000, System.currentTimeMillis(), 300)) {
                    //同步时间
                    String machineId = topic.split("/")[2];
                    MQTTUtil.publishTimeSyncTopic(mqttService, machineId);
                    logger.info("send message to sync time for client: " + machineId);
                    logger.info("Timestamp of the received package elapsed the duration, thus the package is aborted.");
                    return;
                }
            }
            if (message.isDuplicate())
                return;
            handle(topic, json);
        });
    }

    public void connectionLost(Throwable cause) {
        logger.error("[Error] Connection lost because: " + cause);
        subscribe();
    }

    public void deliveryComplete(IMqttDeliveryToken token) {
    }
//    /**
//     * 实现MqttOutbound
//     */
//    @Bean
//    public MqttPahoClientFactory mqttClientFactory() {
//        String[] serverUrls = mqttProperties.getOutbound().getUrls().split(",");
//        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
//        //factory.setUserName(username);
//        //factory.setPassword(password);
//        factory.setServerURIs(serverUrls);
//        factory.setCleanSession(true);
//        factory.setConnectionTimeout(10);
//        factory.setKeepAliveInterval(20);
//        return factory;
//    }
//
//    @Bean
//    @ServiceActivator(inputChannel = "mqttOutboundChannel")
//    public MessageHandler mqttOutbound() {
//        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(mqttProperties.getOutbound().getClientId(), mqttClientFactory());
//        messageHandler.setAsync(true);
//        //messageHandler.setDefaultTopic(mqttProperties.getOutbound().getTopic());
//        return messageHandler;
//    }
//
//    @Bean
//    public MessageChannel mqttOutboundChannel() {
//        return new DirectChannel();
//    }
//
//
//    /**
//     * 实现MqttInbound
//     */
//    @Bean
//    public MessageChannel mqttInputChannel() {
//        return new DirectChannel();
//    }
//
//    @Bean
//    public MessageProducer inbound() {
//        String[] inboundTopic = topicDetail();
//        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(mqttProperties.getInbound().getClientId(), mqttClientFactory(), inboundTopic);
//        adapter.setCompletionTimeout(5000);
//        adapter.setConverter(new DefaultPahoMessageConverter());
//        adapter.setQos(1);
//        adapter.setOutputChannel(mqttInputChannel());
//        return adapter;
//    }
//
//    @Bean
//    @ServiceActivator(inputChannel = "mqttInputChannel")
//    public MessageHandler handler() {
//        return new MessageHandler() {
//            @Override
//            public void handleMessage(Message<?> message) {
//                CorePool.getHandlePool().execute(() -> {
//                    if (message == null) {
//                        logger.error("[Error] illegal message received.");
//                        return;
//                    }
//                    MessageHeaders headers = message.getHeaders();
//                    if (headers == null) return;
//                    //判断是否要丢弃报文
//                    if (TimeUtil.exceed(headers.getTimestamp(), System.currentTimeMillis(), 300)) {
//                        logger.info("Timestamp of the received package elapsed the duration, thus the package is aborted.");
//                        return;
//                    }
//                    String payload = ((String) message.getPayload());
//                    String topic = headers.get("mqtt_topic").toString();
//                    //将payload转换为json数据格式，进行进一步处理
//                    if (headers.containsKey("mqtt_duplicate") && (Boolean) headers.get("mqtt_duplicate") == true)
//                        return;
//                    handle(topic, JSON.parseObject(payload));
//                });
//            }
//        };
//    }

    /**
     * 获取当前需订阅的所有topic
     */
    private String[] topicDetail() {
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        ResultData response = topicService.fetch(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            return null;
        }
        List<Topic> list = (List<Topic>) response.getData();
        String[] result = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i).getTopicDetail();
        }
        return result;
    }

    /**
     * 处理当前message的信息
     */
    private void handle(String topic, JSONObject json) {
        try {
//        logger.info("topic: " + topic);
            //将topic根据"/"切分为string数组，方便处理
            String[] array = topic.split("/");
            //根据定义的topic格式，获取message对应的machineId
            String machineId = array[2];
            String base_action = array[5];
            //对于长度为7的topic，只有上传单项传感器数据和警报处理
            if (array.length == 7) {
                //获取传感器数值类型（pm2.5, co2, temp, temp_out, humidity）其中之一
                String detail_action = array[6];
                switch (base_action) {
                    case "sensor":
                        int value = json.getIntValue("value");
                        dealSingleSensor(detail_action, value);
                        break;
                    case "alert":
                        AlertPayload payload = new AlertPayload(machineId, json);
                        dealAlertMessage(detail_action, payload);
                        break;
                    default:
                        logger.error("Message cannot be handled. Topic: " + topic + ", data: " + json);
                        break;
                }
            } else {
                //该类型的数据报文将存入内存缓存
                if (base_action.equals("allrep")) {
                    messageController.checkVersion(machineId);
//                logger.info("uid: " + machineId + ", allrep: " + json);
                    if (json.containsKey("power") && json.getIntValue("power") == 0) {
                        json.replace("volume", 0);
                    }
                    //写入内存缓存的数据使用common模块中的结构
                    finley.gmair.model.machine.v3.MachineStatusV3 status = new finley.gmair.model.machine.v3.MachineStatusV3(machineId, json);
                    LimitQueue<finley.gmair.model.machine.v3.MachineStatusV3> queue;
                    if (redisService.exists(machineId)) {
                        queue = (LimitQueue) redisService.get(machineId);
                        queue.offer(status);
                    } else {
                        queue = new LimitQueue<>(120);
                        queue.offer(status);
                    }
                    redisService.set(machineId, queue, (long) 120);
                    CorePool.getComPool().execute(() -> {
                        if (!isReplica) {
                            //写入mongodb的使用mongo-common中的结构
                            finley.gmair.model.machine.MachineStatusV3 mongo = new MachineStatusV3(machineId, json);
                            repository.save(mongo);
                        }
                    });
                }
                //该类型的数据报文仅用于更新内存缓存中的数据状态，不存入数据库
                if (base_action.equals("sys_status")) {
                    MQTTUtil.partial(redisService, machineId, json);
                }
                if (base_action.equals("sys_surplus")) {
                    SurplusPayload payload = new SurplusPayload(machineId, json);
//                logger.info("sys_surplus: " + JSON.toJSONString(payload));
                }
                //传感器数据上传
                if (base_action.equals("sensor")) {
                    MQTTUtil.partial(redisService, machineId, json);
                }
                if (base_action.equals("ack")) {
                    AckPayload payload = new AckPayload(machineId, json);
                    if (payload.getCode() != 0) {
                        payload.setError(json.getString("error"));
                    }
                    dealAckMessage(payload);
//                logger.info("ack: " + JSON.toJSONString(payload));
                }
                //服务器端相应设备的同步时钟请求
                if (base_action.equals("get_time")) {
                    MQTTUtil.publishTimeSyncTopic(mqttService, machineId);
                }
                if (base_action.equals("chk_update")) {

                }
                if (base_action.equals("ver")) {

                }
            }
        } catch (Exception e) {
            logger.error("[Error ] " + e.getMessage());
        }
    }

    /**
     * 处理警报消息publish
     */
    private void dealAlertMessage(String detail_action, AlertPayload payload) {
        int code = payload.getCode();
        String machineId = payload.getMachineId();
        String msg = payload.getMsg();
        if (detail_action.equals("pub")) {
            new Thread(() -> {
                machineController.createAlert(machineId, code, msg);
            }).start();
        }
        if (detail_action.equals("rm")) {
            new Thread(() -> {
                machineController.updateAlert(machineId, code);
            }).start();
        }
    }

    /**
     * 处理单个传感器数据消息publish
     */
    private void dealSingleSensor(String detail_action, int value) {
        if (detail_action.equals("pm2.5a")) {}
        if (detail_action.equals("pm2.5b")) {}
        if (detail_action.equals("co2")) {}
        if (detail_action.equals("temp")) {}
        if (detail_action.equals("temp_out")) {}
        if (detail_action.equals("humidity")) {}
    }

    /**
     * 处理ack确认消息publish
     */
    private void dealAckMessage(AckPayload payload) {
//        logger.info("ack: " + JSON.toJSONString(payload));
        if (payload.getCode() == 0) {
//            new Thread(() -> {
//                logService.createMqttAckLog(payload.getAckId(), payload.getMachineId(),
//                        payload.getCode(), "Mqtt", ip, new StringBuffer("The machine: ")
//                                .append(payload.getMachineId()).append("operate the command successfully").toString());
//            }).start();
        }
        if (payload.getCode() != 0) {
            CorePool.getLogPool().execute(() -> logService.createMqttAckLog(payload.getAckId(), payload.getMachineId(), payload.getCode(), "Mqtt", ip, payload.getError())
            );
        }
    }
}
