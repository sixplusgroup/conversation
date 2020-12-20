package finley.gmair.resolve;

import com.alibaba.fastjson.JSONObject;
import finley.gmair.mqtt.MqttUtil;
import finley.gmair.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * sensor行为的处理器
 *
 * @author lycheeshell
 * @date 2020/12/19 12:36
 */
@Component
public class ActionSensorResolver implements ActionResolver, InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(ActionSensorResolver.class);

    @Resource
    private RedisService redisService;

    @Override
    public void afterPropertiesSet() {
        ActionResolverFactory.register("SENSOR", this);
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
        String furtherAction;
        if (topicArray.length == 7) {
            //获取传感器数值类型（pm2.5, co2, temp, temp_out, humidity）其中之一
            furtherAction = topicArray[6];
            int value = (json.containsKey("value")) ? json.getIntValue("value") : Integer.MIN_VALUE;
            dealSingleSensor(furtherAction, value);
        } else if (topicArray.length == 6) {
            MqttUtil.partial(redisService, machineId, json);
        } else {
            logger.error("[Error] sensor action, topicArray length is " + topicArray.length + " , topic is " + topic);
        }
    }

    /**
     * 处理单个传感器数据消息publish，暂不处理
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

}
