package finley.gmair.scene.service.impl;

import com.alibaba.fastjson.JSON;
import finley.gmair.scene.client.MachineClient;
import finley.gmair.scene.constant.ErrorCode;
import finley.gmair.scene.dao.SceneOperationDAO;
import finley.gmair.scene.dto.SceneOperationDTO;
import finley.gmair.scene.entity.SceneOperationCommand;
import finley.gmair.scene.entity.SceneOperationDO;
import finley.gmair.scene.service.SceneOperationService;
import finley.gmair.scene.utils.BizException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author : Lyy
 * @create : 2020-12-04 17:37
 **/
@Service
@Slf4j
public class SceneOperationServiceImpl implements SceneOperationService {

    @Resource
    private SceneOperationDAO sceneOperationDAO;

    @Resource
    private MachineClient machineClient;

    @Override
    public boolean createSceneOperation(SceneOperationDTO sceneOperationDTO) {
        SceneOperationDO sceneOperationDO = new SceneOperationDO();
        BeanUtils.copyProperties(sceneOperationDTO, sceneOperationDO);
        // 获取场景ID
        sceneOperationDO.setSceneId(sceneOperationDTO.getSceneId());
        log.info("sceneOperationDO: {}", JSON.toJSONString(sceneOperationDO));
        SceneOperationDO tmp = sceneOperationDAO.insertSceneOperation(sceneOperationDO);
        if (tmp.getId().isEmpty()) {
            // 场景命令写入失败
            // todo 打日志
            log.error("create scene operations failed");
            throw new BizException(ErrorCode.UNKNOWN_ERROR);
        }
        return true;
    }

    @Override
    public boolean updateSceneOperation(SceneOperationDTO sceneOperationDTO) {
        SceneOperationDO sceneOperationDO = sceneOperationDAO.selectSceneOperationBySceneId(sceneOperationDTO.getSceneId());
        sceneOperationDO.setSceneName(sceneOperationDTO.getSceneName());
        sceneOperationDO.setCommands(sceneOperationDTO.getCommands());
        sceneOperationDO.setConsumerId(sceneOperationDTO.getConsumerId());
        // todo 校验下这里是不是真的更新
        SceneOperationDO tmp = sceneOperationDAO.updateSceneOperation(sceneOperationDO);
        if (tmp.getId().isEmpty()) {
            // 更新失败
            log.error("update scene operations failed");
            throw new BizException(ErrorCode.UNKNOWN_ERROR);
        }
        return true;
    }

    @Override
    public boolean deleteSceneOperationBySceneId(long sceneId) {
        SceneOperationDO sceneOperationDO = sceneOperationDAO.deleteSceneOperation(sceneId);
        if (!sceneOperationDO.isDeleted()) {
            // 删除失败
            log.error("delete scene operations failed");
            throw new BizException(ErrorCode.UNKNOWN_ERROR);
        }
        return true;
    }

    @Override
    public int deleteScenesOperationsByConsumerId(String consumerId) {
        return sceneOperationDAO.deleteSceneOperationsByConsumerId(consumerId);
    }

    @Override
    public SceneOperationDTO getOperationBySceneId(long sceneId) {
        SceneOperationDO sceneOperationDO = sceneOperationDAO.selectSceneOperationBySceneId(sceneId);
        SceneOperationDTO sceneOperationDTO = new SceneOperationDTO();
        BeanUtils.copyProperties(sceneOperationDO, sceneOperationDTO);
        return sceneOperationDTO;
    }

    @Override
    public List<String> getQrCodesBySceneId(long sceneId) {
        SceneOperationDTO sceneOperationDTO = getOperationBySceneId(sceneId);
        return getQrCodesBySceneId(sceneOperationDTO);
    }

    @Override
    public List<String> getQrCodesBySceneId(SceneOperationDTO sceneOperationDTO) {
        List<SceneOperationCommand> commands = sceneOperationDTO.getCommands();
        return commands.stream().map(SceneOperationCommand::getQrCode).distinct().collect(Collectors.toList());
    }

    // 执行操作
    @Override
    public void executeOperation(SceneOperationDTO sceneOperationDTO) {
        String[] configArr = {"speed", "light", "timing", "temp"};
        sceneOperationDTO.getCommands().forEach(command -> {
            String component = command.getCommandComponent();
            String operation = command.getCommandOperation();
            log.info("component is: {}, operation is: {}", component, operation);
            // todo 用消息队列发送指令
            if (ArrayUtils.contains(configArr, component)) {
                if (!StringUtils.isNumeric(operation)) {
                    return;
                }
                Map<String, Integer> queryMap = new HashMap<>();
                queryMap.put(component, NumberUtils.toInt(operation));
                machineClient.config(command.getQrCode(), component, queryMap);
            } else {
                machineClient.operate(command.getQrCode(), component, operation);
            }
//            ResultData resultData = machineClient.chooseComponent(command.getQrCode(), component, operation);
//            if (!resultData.getResponseCode().equals(ResponseCode.RESPONSE_OK)) {
//                // todo 打日志记录下来
//                log.error("command execute failed, qrCode:{}", command.getQrCode());
//                System.out.println("日志记录某设备操作失败");
//            }
        });
    }

}
