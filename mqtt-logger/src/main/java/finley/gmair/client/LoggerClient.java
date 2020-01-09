package finley.gmair.client;

import finley.gmair.model.mqtt.LoggerRecord;
import finley.gmair.pool.CorePool;
import finley.gmair.service.LoggerRecordService;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.MqttProperties;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.*;

import javax.annotation.Resource;
import java.sql.Timestamp;

//@PropertySource({"classpath:auth.properties", "classpath:mqtt.properties"})
@Configuration
public class LoggerClient {
    private Logger logger = LoggerFactory.getLogger(LoggerClient.class);

//    private String[] inboundTopic = {"/client/FA/update"};
//    private String inboundUrl = "tcp://192.168.1.105:1883";
//    private String clientId = "logger-client";

    @Autowired
    private MqttProperties mqttProperties;

    @Resource
    private LoggerRecordService loggerRecordService;

    @Bean
    public MessageChannel mqttInputChannel(){
        return new DirectChannel();
    }

    @Bean
    public MessageProducer inbound(){
        String[] inboundTopic = mqttProperties.getInbound().getTopics().split(",");
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(mqttProperties.getInbound().getUrl(), mqttProperties.getInbound().getClientId()
        , inboundTopic);
        //MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(inboundUrl, clientId, inboundTopic);
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler(){
        return new MessageHandler() {

            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                System.out.println(message.toString());
                CorePool.getHandlePool().execute(() ->{
                    System.out.println("corepool");
                    if (message == null) {
                        logger.error("[Error] illegal message received.");
                        return;
                    }
                    MessageHeaders headers = message.getHeaders();
                    if (headers == null) return;
                    String topic = headers.get("mqtt_topic").toString();
                    if (topic.startsWith("/")) topic = topic.substring(1);
                    String[] array = topic.split("/");
                    //根据定义的topic格式，获取message对应的machineId
                    String machineId = array[2];
                    Long timestamp = headers.getTimestamp();
                    String payload = ((String) message.getPayload());
                    if(StringUtils.isBlank(payload)){
                        payload = "no message";
                    }
                    LoggerRecord loggerRecord = new LoggerRecord();
                    loggerRecord.setPayloadContext(payload);
                    loggerRecord.setTopicContext(topic);
                    loggerRecord.setCreateAt(new Timestamp(timestamp));
                    loggerRecord.setRecordId(IDGenerator.generate("1"));
                    loggerRecord.setMachineId(machineId);
                    //loggerRecord.setBlockFlag(false);
                    try{
                        loggerRecordService.create(loggerRecord);
                        System.out.println("insert");
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                });

            }
        };
    }


}
