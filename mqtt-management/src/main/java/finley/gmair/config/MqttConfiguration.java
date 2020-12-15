package finley.gmair.config;

import finley.gmair.model.mqttManagement.Topic;
import finley.gmair.mqtt.MqttProperties;
import finley.gmair.service.TopicService;
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

    @Value("${username}")
    private String username;

    @Value("${password}")
    private String password;

    @Value("${inbound_url}")
    private String ip;

    @Value("${replica}")
    private boolean isReplica;

    @Resource
    private MqttProperties mqttProperties;

    @Resource
    private TopicService topicService;

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
        String[] inboundTopics = getInboundTopics();
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(mqttProperties.getInbound().getClientId(), mqttClientFactory(), inboundTopics);
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(0);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler() {
        return new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) {
                //todo
            }
        };
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

}
