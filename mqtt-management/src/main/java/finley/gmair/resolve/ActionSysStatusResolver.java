package finley.gmair.resolve;

import com.alibaba.fastjson.JSONObject;
import finley.gmair.mqtt.MqttUtil;
import finley.gmair.service.RedisService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * SYS_STATUS行为的处理器
 *
 * @author lycheeshell
 * @date 2020/12/20 20:34
 */
@Component
public class ActionSysStatusResolver implements ActionResolver, InitializingBean {

    @Resource
    private RedisService redisService;

    @Override
    public void afterPropertiesSet() {
        ActionResolverFactory.register("SYS_STATUS", this);
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
        //该类型的数据报文仅用于更新内存缓存中的数据状态，不存入数据库
        MqttUtil.partial(redisService, machineId, json);
    }

}
