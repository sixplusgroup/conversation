package finley.gmair.scene.mq;

import com.alibaba.fastjson.JSON;
import com.maihaoche.starter.mq.annotation.MQConsumer;
import com.maihaoche.starter.mq.base.AbstractMQPushConsumer;
import finley.gmair.scene.client.MachineClient;
import finley.gmair.scene.dto.SceneOperationDTO;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : Lyy
 * @create : 2021-01-14 15:36
 * @description 场景控制消息接收者
 **/
@Slf4j
@Service
@MQConsumer(topic = "scene-operation", consumerGroup = "scene-consumer-group-1")
public class SceneOperationConsumer extends AbstractMQPushConsumer<SceneOperationDTO> {

    @Resource
    private MachineClient machineClient;

    // 执行场景时，用这个logger记录执行结果，会自动保存到mongodb中
    private final Logger logger = LoggerFactory.getLogger("scene.operation.log");

    @Override
    public boolean process(SceneOperationDTO sceneOperationDTO, Map<String, Object> map) {
        log.info("msg receive,sceneOperationDTO is: {}", JSON.toJSONString(sceneOperationDTO));
        String[] configArr = {"speed", "light", "timing", "temp"};
        sceneOperationDTO.getCommands().forEach(command -> {
            String component = command.getCommandComponent();
            String operation = command.getCommandOperation();
            log.info("component is: {}, operation is: {}", component, operation);

            ResultData resultData = null;
            if (ArrayUtils.contains(configArr, component)) {
                if (!StringUtils.isNumeric(operation)) {
                    return;
                }
                Map<String, Integer> queryMap = new HashMap<>();
                queryMap.put(component, NumberUtils.toInt(operation));
                resultData = machineClient.config(command.getQrCode(), component, queryMap);
            } else {
                resultData = machineClient.operate(command.getQrCode(), component, operation);
            }
            if (!resultData.getResponseCode().equals(ResponseCode.RESPONSE_OK)) {
                // todo 命令执行失败打日志记录下来
                log.error("command execute failed, qrCode:{}", command.getQrCode());
                System.out.println("日志记录某设备操作失败");
            } else {
                // todo 命令执行成功，日志记录
                log.info("command success,component is: {}", component);
            }
        });
        return true;
    }

}
