package finley.gmair.mqtt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import finley.gmair.check.ActionChecker;
import finley.gmair.check.ModelChecker;
import finley.gmair.model.mqttManagement.Topic;
import finley.gmair.pool.CorePool;
import finley.gmair.resolve.ActionResolver;
import finley.gmair.resolve.ActionResolverFactory;
import finley.gmair.service.TopicService;
import finley.gmair.util.TimeUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessageHeaders;

import javax.annotation.Resource;
import java.util.List;

/**
 * mqtt配置管理类
 *
 * @author lycheeshell
 * @date 2020/12/3 15:23
 */
@PropertySource({"classpath:auth.properties", "classpath:mqtt.properties"})
@Configuration
public class MqttConfiguration {

    private final Logger logger = LoggerFactory.getLogger(MqttConfiguration.class);

    @Value("${username}")
    private String username;

    @Value("${password}")
    private String password;

    @Value("${open_check}")
    private boolean isCheckOpen;

    @Resource
    private MqttProperties mqttProperties;

    @Resource
    private MqttGateway mqttGateway;

    @Resource
    private ModelChecker modelChecker;

    @Resource
    private ActionChecker actionChecker;

    @Resource
    private TopicService topicService;

    private MqttPahoMessageDrivenChannelAdapter adapter;

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

//    ----------------------------- 实现MqttOutbound start -----------------------------

    @Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutbound() {
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(mqttProperties.getOutbound().getClientId(), mqttClientFactory());
        messageHandler.setAsync(true);
        messageHandler.setDefaultTopic(mqttProperties.getOutbound().getTopic());
        return messageHandler;
    }

//     ----------------------------- 实现MqttOutbound end -----------------------------

//     ----------------------------- 实现MqttInbound start -----------------------------

    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageProducer inbound() {
        String[] inboundTopics = getInboundTopics();
        adapter = new MqttPahoMessageDrivenChannelAdapter(mqttProperties.getInbound().getClientId(), mqttClientFactory(), inboundTopics);
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(0);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }

//     ----------------------------- 实现MqttInbound end -----------------------------

    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler() {
        return message -> CorePool.getHandlePool().execute(() -> {
            if (message == null) {
                logger.error("[Error] illegal message received.");
                return;
            }
            MessageHeaders headers = message.getHeaders();
            if (headers == null) {
                return;
            }

            //消息主题
            String topic = headers.get("mqtt_topic").toString();
            if (topic == null) {
                return;
            }
            String[] topicArray = topic.split("/");
            if (topicArray.length < 6) {
                return;
            }

            //判断是否要丢弃报文
            if (TimeUtil.exceed(headers.getTimestamp(), System.currentTimeMillis(), 300)) {
                //根据定义的topic格式，获取message对应的machineId
                String machineId = topicArray[2];
                MqttUtil.publishTimeSyncTopic(mqttGateway, machineId);
                logger.info("Timestamp of the received package elapsed the duration, thus the package is aborted. Topic: " + topic);
                logger.info("send message to sync time for client: " + machineId);
                return;
            }
            if (headers.containsKey("mqtt_duplicate") && (Boolean) headers.get("mqtt_duplicate")) {
                return;
            }

            //消息内容
            String payload = ((String) message.getPayload());

            //检查消息格式及内容的正确性，抛弃恶意攻击的消息报文
            if (isCheckOpen && (!isMessageNormative(topic, payload))) {
                return;
            }

            //将payload转换为json数据格式，进行进一步处理
            handleMessage(topic, JSON.parseObject(payload));
        });
    }

    /**
     * 处理消息
     *
     * @param topic 消息主题
     * @param payload 消息内容
     */
    private void handleMessage(String topic, JSONObject payload) {
        String[] topicArray = topic.split("/");

        //统一处理时间超出的情况
        if (payload.containsKey("time")) {
            if (TimeUtil.exceed(payload.getLong("time") * 1000, System.currentTimeMillis(), 300)) {
                //同步时间
                String machineId = topicArray[2];
                MqttUtil.publishTimeSyncTopic(mqttGateway, machineId);
                logger.info("Timestamp of the received package elapsed the duration, thus the package is aborted. Topic: " + topic + ", payload: " + payload);
                logger.info("send message to sync time for client: " + machineId);
                return;
            }
        }

        String action = topicArray[5].toUpperCase();
        ActionResolver actionResolver = ActionResolverFactory.getActionResolver(action);
        if (actionResolver != null) {
            actionResolver.resolve(topic, payload);
        } else {
            logger.warn("can not find the resolver of action : " + action);
        }
    }

    /**
     * 检查消息格式及内容的正确性，抛弃恶意攻击的消息
     *
     * @param topic   消息主题
     * @param payload 消息内容
     * @return 消息是否正确
     */
    private boolean isMessageNormative(String topic, String payload) {
        String[] topicArray = topic.split("/");

        String model = topicArray[1];
        String action = topicArray[5];

        return modelChecker.isModelHaveAction(model, action) && actionChecker.isJsonCorrectInAction(action, payload);
    }

    /**
     * 获取当前需订阅的所有topic
     *
     * @return 订阅的主题组成的数组
     */
    private String[] getInboundTopics() {
        List<Topic> topics = topicService.queryTopics(null, null, null);
        if (topics != null && topics.size() > 0) {
            String[] result = new String[topics.size()];
            for (int i = 0; i < topics.size(); i++) {
                result[i] = topics.get(i).getTopicDetail();
            }
            return result;
        } else {
            throw new RuntimeException("数据库里订阅的topic数量为0");
        }
    }

    /**
     * 添加订阅的主题
     *
     * @param topics 新加的主题
     */
    public void addInboundTopic(String[] topics) {
        checkAndInitAdapter();
        if (topics != null) {
            for (String topic : topics) {
                if (StringUtils.isNotEmpty(topic)) {
                    adapter.addTopic(topic, 0);
                }
            }
        }
    }

    /**
     * 取消主题的订阅
     *
     * @param topics 取消订阅的主题
     */
    public void removeInboundTopic(String[] topics) {
        checkAndInitAdapter();
        if (topics != null) {
            adapter.removeTopic(topics);
        }
    }

    /**
     * 检查adapter是否没有初始化，如果没有初始化则进行初始化
     */
    private void checkAndInitAdapter() {
        if (adapter == null) {
            String[] inboundTopics = getInboundTopics();
            adapter = new MqttPahoMessageDrivenChannelAdapter(mqttProperties.getInbound().getClientId(), mqttClientFactory(), inboundTopics);
            adapter.setCompletionTimeout(5000);
            adapter.setConverter(new DefaultPahoMessageConverter());
            adapter.setQos(0);
            adapter.setOutputChannel(mqttInputChannel());
        }
    }

}
