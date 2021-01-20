package finley.gmair.scene.mq;

import com.maihaoche.starter.mq.annotation.MQConsumer;
import com.maihaoche.starter.mq.base.AbstractMQPushConsumer;
import finley.gmair.scene.client.MachineClient;
import finley.gmair.scene.dto.SceneOperationDTO;
import finley.gmair.scene.entity.SceneOperationCommand;
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
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        String[] configArr = {"speed", "light", "timing", "temp"};
        // 先根据sequence对指令顺序进行排序
        List<SceneOperationCommand> commands = sceneOperationDTO.getCommands().stream()
                .sorted(Comparator.comparing(SceneOperationCommand::getSequence)).collect(Collectors.toList());
        for (SceneOperationCommand command : commands) {
            String device = command.getDeviceName();
            String component = command.getCommandComponent();
            String operation = command.getCommandOperation();
            log.info("device is:{},component is: {}, operation is: {}", device, component, operation);
            ResultData resultData = null;
            if (ArrayUtils.contains(configArr, component)) {
                if (!StringUtils.isNumeric(operation)) {
                    continue;
                }
                int value = NumberUtils.toInt(operation);
                switch (component) {
                    case "speed":
                        resultData = machineClient.configSpeed(command.getQrCode(), value);
                        break;
                    case "light":
                        resultData = machineClient.configLight(command.getQrCode(), value);
                        break;
                    case "timing":
                        resultData = machineClient.configTiming(command.getQrCode(), value);
                        break;
                    case "temp":
                        resultData = machineClient.configTemp(command.getQrCode(), value);
                        break;
                }
            } else {
                resultData = machineClient.operate(command.getQrCode(), component, operation);
            }
            if (!resultData.getResponseCode().equals(ResponseCode.RESPONSE_OK)) {
                // todo 命令执行失败打日志记录下来
                logger.error("command execute failed, device:{}", device);
            } else {
                // todo 命令执行成功，日志记录
                logger.info("command success,the device is: {}", device);
            }
        }
        return true;
    }
}
