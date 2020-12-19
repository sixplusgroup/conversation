package finley.gmair.resolve;

import com.alibaba.fastjson.JSONObject;
import finley.gmair.model.mqttManagement.payload.AlertPayload;
import finley.gmair.pool.CorePool;
import finley.gmair.service.MachineAlertService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * chk_update行为的处理器
 *
 * @author lycheeshell
 * @date 2020/12/19 12:30
 */
@Component
public class ActionChkUpdateResolver implements ActionResolver, InitializingBean {


    @Override
    public void afterPropertiesSet() {
        ActionResolverFactory.register("CHK_UPDATE", this);
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
