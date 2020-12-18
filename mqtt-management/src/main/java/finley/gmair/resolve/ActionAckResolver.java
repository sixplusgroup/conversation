package finley.gmair.resolve;

import com.alibaba.fastjson.JSONObject;
import finley.gmair.model.mqttManagement.payload.AckPayload;
import finley.gmair.pool.CorePool;
import finley.gmair.service.LogService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * ack行为的处理器
 *
 * @author lycheeshell
 * @date 2020/12/8 15:40
 */
@PropertySource({"classpath:mqtt.properties"})
@Component
public class ActionAckResolver implements ActionResolver, InitializingBean {

    @Value("${inbound_url}")
    private String ip;

    @Resource
    private LogService logService;

    @Override
    public void afterPropertiesSet() {
        ActionResolverFactory.register("ACK", this);
    }

    /**
     * 处理行为
     *
     * @param topic mqtt主题topic，topic的第一个"/"已经删除
     * @param json  mqtt消息内容payload的json
     */
    @Override
    public void resolve(String topic, JSONObject json) {
        String[] topicArray = topic.split("/");
        String machineId = topicArray[2];
        AckPayload payload = new AckPayload(machineId, json);
        if (payload.getCode() != 0) {
            payload.setError(json.getString("error"));
            CorePool.getLogPool().execute(() -> logService.createMqttAckLog(payload.getAckId(), payload.getMachineId(),
                    payload.getCode(), "Mqtt", ip, payload.getError())
            );
        } else {
            CorePool.getLogPool().execute(() -> logService.createMqttAckLog(payload.getAckId(), payload.getMachineId(),
                    payload.getCode(), "mqtt", ip,
                    "The machine: " + payload.getMachineId() + "operate the command successfully"));
        }
    }

}
