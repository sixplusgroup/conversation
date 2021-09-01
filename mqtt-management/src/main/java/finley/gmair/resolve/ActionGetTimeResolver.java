package finley.gmair.resolve;

import com.alibaba.fastjson.JSONObject;
import finley.gmair.mqtt.MqttGateway;
import finley.gmair.mqtt.MqttUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * get_time行为的处理器
 *
 * @author lycheeshell
 * @date 2020/12/20 20:31
 */
@Component
public class ActionGetTimeResolver implements ActionResolver, InitializingBean {

    @Resource
    private MqttGateway mqttGateway;

    @Override
    public void afterPropertiesSet() {
        ActionResolverFactory.register("GET_TIME", this);
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
        //获取服务器时间
        MqttUtil.publishTimeSyncTopic(mqttGateway, machineId);
    }

}
