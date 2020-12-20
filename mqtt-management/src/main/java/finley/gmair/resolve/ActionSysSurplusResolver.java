package finley.gmair.resolve;

import com.alibaba.fastjson.JSONObject;
import finley.gmair.model.mqtt.SurplusPayload;
import finley.gmair.pool.CorePool;
import finley.gmair.service.MachineFeignService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * SYS_SURPLUS行为的处理器
 *
 * @author lycheeshell
 * @date 2020/12/20 20:37
 */
@Component
public class ActionSysSurplusResolver implements ActionResolver, InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(ActionSysSurplusResolver.class);

    @Resource
    private MachineFeignService machineFeignService;

    @Override
    public void afterPropertiesSet() {
        ActionResolverFactory.register("SYS_SURPLUS", this);
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
        //滤网时间处理
        SurplusPayload surplus = new SurplusPayload(machineId, json);
        CorePool.getSurplusPool().execute(() -> {
            try {
                machineFeignService.updateByRemain(surplus.getRemain(), surplus.getMachineId());
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e.getMessage());
            }
        });
    }

}
