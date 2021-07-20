package finley.gmair.resolve;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * VER行为的处理器
 *
 * @author lycheeshell
 * @date 2020/12/20 20:45
 */
@Component
public class ActionVerResolver implements ActionResolver, InitializingBean {


    @Override
    public void afterPropertiesSet() {
        ActionResolverFactory.register("VER", this);
    }

    /**
     * 处理行为
     *
     * @param topic mqtt主题topic，topic的第一个"/"已经删除
     * @param json  mqtt消息内容payload的json
     */
    @Override
    public void resolve(String topic, JSONObject json) {
    }

}
