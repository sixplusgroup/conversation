package finley.gmair.resolve;

import com.alibaba.fastjson.JSONObject;
import finley.gmair.model.mqttManagement.payload.AlertPayload;
import finley.gmair.pool.CorePool;
import finley.gmair.service.MachineAlertService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * alert行为的处理器
 *
 * @author lycheeshell
 * @date 2020/12/18 15:48
 */
@Component
public class ActionAlertResolver implements ActionResolver, InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(ActionAlertResolver.class);

    @Resource
    private MachineAlertService machineAlertService;

    @Override
    public void afterPropertiesSet() {
        ActionResolverFactory.register("ALERT", this);
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
        if (topicArray.length < 7) {
            logger.error("[Error] alert action, topicArray length is " + topicArray.length + " , topic is " + topic);
            return;
        }
        String furtherAction = topicArray[6];
        AlertPayload alert = new AlertPayload(machineId, json);
        int code = alert.getCode();
        String msg = alert.getMsg();

        String pubAlert = "pub";
        if (pubAlert.equals(furtherAction)) {
            CorePool.getLogPool().execute(() -> machineAlertService.createMachineAlert(machineId, code, msg));
        }
        String rmAlert = "rm";
        if (rmAlert.equals(furtherAction)) {
            CorePool.getLogPool().execute(() -> machineAlertService.updateMachineAlert(machineId, code));
        }
    }

}
