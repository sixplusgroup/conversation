package finley.gmair.resolve;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * RESPONSE行为的处理器
 *
 * @author lycheeshell
 * @date 2020/12/20 20:48
 */
@Component
public class ActionResponseResolver implements ActionResolver, InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(ActionResponseResolver.class);

    @Override
    public void afterPropertiesSet() {
        ActionResolverFactory.register("RESPONSE", this);
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
        logger.info(" turbo action response received, mac: " + machineId + ", payload: " + JSON.toJSONString(json));
    }

}
