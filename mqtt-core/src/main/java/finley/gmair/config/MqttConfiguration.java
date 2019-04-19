package finley.gmair.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import finley.gmair.controller.MachineController;
import finley.gmair.model.mqtt.*;
import finley.gmair.service.LogService;
import finley.gmair.service.TopicService;
import finley.gmair.util.MqttProperties;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
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
import org.springframework.messaging.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@PropertySource("classpath:auth.properties")
@Configuration
public class MqttConfiguration {

    @Autowired
    private MqttProperties mqttProperties;

    @Autowired
    private TopicService topicService;

    @Autowired
    private MachineController machineController;

    @Autowired
    private LogService logService;

    private final static String ip = "116.62.233.170";

    @Value("${username}")
    private String username;

    @Value("${password}")
    private String password;

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
        MqttPahoMessageHandler messageHandler =
                new MqttPahoMessageHandler(mqttProperties.getOutbound().getClientId(), mqttClientFactory());
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
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(mqttProperties.getInbound().getClientId(), mqttClientFactory(),
                        inboundTopic);
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
            public void handleMessage(Message<?> message) throws MessagingException {
//                System.out.println(message.getHeaders());
//                System.out.println(message.getPayload());
                MessageHeaders headers = message.getHeaders();
                String payload = ((String) message.getPayload());
                String topic = headers.get("mqtt_topic").toString();

                //将payload转换为json数据格式，进行进一步处理
                JSONObject json = JSON.parseObject(payload);
                handle(topic, json);
            }
        };
    }

    /**
     * 获取当前需订阅的所有topic
     * */
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
     * */
    private void handle(String topic, JSONObject json) {
        //将topic根据"/"切分为string数组，方便处理
        String[] array = topic.split("/");
        //根据定义的topic格式，获取message对应的machineId
        String machineId = array[2];
        String base_action = array[5];
        //对于长度为7的topic，只有上传单项传感器数据和警报处理
        if (array.length == 7) {
            //获取传感器数值类型（pm2.5, co2, temp, temp_out, humidity）其中之一
            String detail_action = array[6];
            if (base_action.equals("sensor")) {
                int value = json.getIntValue("value");
                dealSingleSensor(detail_action, value);
            }
            if (base_action.equals("alert")) {
                AlertPayload payload = new AlertPayload(machineId, json);
                dealAlertMessage(detail_action, payload);
            }
        } else {
            if (base_action.equals("sys_status")) {
                StatusPayload payload = new StatusPayload(machineId, json);
                //todo something
            }
            if (base_action.equals("sys_surplus")) {
                SurplusPayload payload = new SurplusPayload(machineId, json);
                //todo something
            }
            if (base_action.equals("sensor")) {
                SensorPayload payload = new SensorPayload(machineId, json);
                //todo something
            }
            if (base_action.equals("ack")) {
                AckPayload payload = new AckPayload(machineId, json);
                if (payload.getCode() != 0) {
                    payload.setError(json.getString("error"));
                }
                dealAckMessage(payload);
            }
            if (base_action.equals("get_time")) {
                //todo set_time interface with machineId
            }
            if (base_action.equals("chk_update")) {
                //todo update interface with machineId
            }
        }
    }

    /**
     * 处理警报消息publish
     * */
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
     * */
    private void dealSingleSensor(String detail_action, int value) {
        if (detail_action.equals("pm2.5")) {

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
     * */
    private void dealAckMessage(AckPayload payload) {
        if (payload.getCode() == 0) {
            new Thread(() -> {
                logService.createMqttAckLog(payload.getAckId(), payload.getMachineId(),
                        payload.getCode(), "Mqtt", ip, new StringBuffer("The machine: ")
                                .append(payload.getMachineId()).append("operate the command successfully").toString());
            }).start();
        }
        if (payload.getCode() != 0) {
            new Thread(() -> {
                logService.createMqttAckLog(payload.getAckId(), payload.getMachineId(),
                        payload.getCode(), "Mqtt", ip, payload.getError());
            }).start();
        }
    }
}
