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

import java.util.HashMap;
import java.util.List;
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
    private MachineController machineController;

    @Autowired
    private LogService logService;

    @Autowired
    private MqttService mqttService;

    @Autowired
    private MessageController messageController;

//    private MqttClient client;
//
//    private static final int[] qos = {2};

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
                        if (topic.startsWith("/")) topic = topic.substring(1);
                        String[] array = topic.split("/");
                        //根据定义的topic格式，获取message对应的machineId
                        String machineId = array[2];
                        MQTTUtil.publishTimeSyncTopic(mqttService, machineId);
                        return;
                    }
                    String payload = ((String) message.getPayload());
                    //将payload转换为json数据格式，进行进一步处理
                    if (headers.containsKey("mqtt_duplicate") && (Boolean) headers.get("mqtt_duplicate") == true)
                        return;
                    handle(topic, JSON.parseObject(payload));
                });
            }
        };
    }

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
            if (topic.startsWith("/")) topic = topic.substring(1);
            //将topic根据"/"切分为string数组，方便处理
            String[] array = topic.split("/");
            //根据定义的topic格式，获取message对应的machineId
            String machineId = array[2];
//            logger.info("machineId: " + machineId);
            //判断是否要丢弃报文
            if (json.containsKey("time")) {
                if (TimeUtil.exceed(json.getLong("time") * 1000, System.currentTimeMillis(), 300)) {
                    //同步时间
                    MQTTUtil.publishTimeSyncTopic(mqttService, machineId);
                    logger.info("send message to sync time for client: " + machineId);
                    logger.info("Timestamp of the received package elapsed the duration, thus the package is aborted.");
                    return;
                }
            }
            String baseAction = array[5];
//            logger.info("base action: " + baseAction);
            String furtherAction;
            switch (baseAction.toUpperCase()) {
                case "ACK":
                    AckPayload payload = new AckPayload(machineId, json);
                    if (payload.getCode() != 0) {
                        payload.setError(json.getString("error"));
                    }
                    dealAckMessage(payload);
                    break;
                case "ALERT":
                    furtherAction = array[6];
                    AlertPayload alert = new AlertPayload(machineId, json);
                    dealAlertMessage(furtherAction, alert);
                    break;
                case "ALLREP":
                    if (json.containsKey("power") && json.getIntValue("power") == 0) {
                        json.replace("speed", 0);
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
                    break;
                case "CHK_UPDATE":
                    break;
                case "SENSOR":
                    if (array.length == 6) {
                        //获取传感器数值类型（pm2.5, co2, temp, temp_out, humidity）其中之一
                        furtherAction = array[6];
                        int value = (json.containsKey("value")) ? json.getIntValue("value") : Integer.MIN_VALUE;
                        dealSingleSensor(furtherAction, value);
                    } else if (array.length == 7) {
                        MQTTUtil.partial(redisService, machineId, json);
                    } else {

                    }
                    break;
                case "GET_TIME":
                    //获取服务器时间
                    MQTTUtil.publishTimeSyncTopic(mqttService, machineId);
                    break;
                case "SYS_STATUS":
                    //该类型的数据报文仅用于更新内存缓存中的数据状态，不存入数据库
                    MQTTUtil.partial(redisService, machineId, json);
                    break;
                case "SYS_SURPLUS":
                    //todo 滤网时间处理
                    SurplusPayload surplus = new SurplusPayload(machineId, json);
//                    logger.info("surplus: " + JSON.toJSONString(surplus));
                    break;
                case "VER":
                    break;
                case "RESPONSE":
                    logger.info(" turbo action response received, mac: " + machineId + ", payload: " + JSON.toJSONString(json));
                    break;
                case "TURBO":
                    logger.info(" turbo action response received, mac: " + machineId + ", payload: " + JSON.toJSONString(json));
                    break;
                default:
                    logger.info("unrecognized action: " + baseAction);
            }
        } catch (Exception e) {
//            logger.error("[Error ] " + e.getMessage());
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
        if (detail_action.equals("pm2.5a")) {
        }
        if (detail_action.equals("pm2.5b")) {
        }
        if (detail_action.equals("co2")) {
        }
        if (detail_action.equals("temp")) {
        }
        if (detail_action.equals("temp_out")) {
        }
        if (detail_action.equals("humidity")) {
        }
    }

    /**
     * 处理ack确认消息publish
     */
    private void dealAckMessage(AckPayload payload) {
        if (payload.getCode() == 0) {
            CorePool.getLogPool().execute(() -> logService.createMqttAckLog(payload.getAckId(), payload.getMachineId(),
                    payload.getCode(), "mqtt", ip, new StringBuffer("The machine: ")
                            .append(payload.getMachineId()).append("operate the command successfully").toString()));
        }
        if (payload.getCode() != 0) {
            CorePool.getLogPool().execute(() -> logService.createMqttAckLog(payload.getAckId(), payload.getMachineId(), payload.getCode(), "Mqtt", ip, payload.getError())
            );
        }
    }
}
