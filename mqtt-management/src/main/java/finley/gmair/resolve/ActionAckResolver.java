package finley.gmair.resolve;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * ack行为的处理器
 *
 * @author lycheeshell
 * @date 2020/12/8 15:40
 */
@Component
public class ActionAckResolver implements ActionResolver, InitializingBean {

    @Override
    public void afterPropertiesSet() {
        ActionResolverFactory.register("ack", this);
    }

    /**
     * 处理行为
     *
     * @param topic mqtt主题topic
     * @param json  mqtt消息内容payload的json
     */
    @Override
    public void resolve(String topic, JSONObject json) {

    }

}
