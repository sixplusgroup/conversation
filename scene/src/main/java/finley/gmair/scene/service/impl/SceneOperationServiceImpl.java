package finley.gmair.scene.service.impl;

import finley.gmair.scene.client.MachineControlClient;
import finley.gmair.scene.dao.SceneOperationDAO;
import finley.gmair.scene.dto.SceneOperationDTO;
import finley.gmair.scene.entity.SceneOperationCommand;
import finley.gmair.scene.entity.SceneOperationDO;
import finley.gmair.scene.service.SceneOperationService;
import finley.gmair.scene.utils.BizException;
import finley.gmair.scene.utils.ErrorCode;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : Lyy
 * @create : 2020-12-04 17:37
 **/
@Service
public class SceneOperationServiceImpl implements SceneOperationService {

    @Resource
    private SceneOperationDAO sceneOperationDAO;

    @Resource
    private MachineControlClient machineControlClient;

    @Override
    public boolean createSceneOperation(SceneOperationDTO sceneOperationDTO) {
        SceneOperationDO sceneOperationDO = new SceneOperationDO();
        BeanUtils.copyProperties(sceneOperationDTO, sceneOperationDO);
        // 获取场景ID
        sceneOperationDO.setSceneId(sceneOperationDTO.getSceneId());
        SceneOperationDO tmp = sceneOperationDAO.insertSceneOperation(sceneOperationDO);
        if (tmp.getId().isEmpty()) {
            // 场景命令写入失败
            // todo 打日志
            throw new BizException(ErrorCode.UNKNOWN_ERROR);
        }
        return true;
    }

    @Override
    public boolean updateSceneOperation(SceneOperationDTO sceneOperationDTO) {
        SceneOperationDO sceneOperationDO = new SceneOperationDO();
        BeanUtils.copyProperties(sceneOperationDO, sceneOperationDTO);
        // todo 校验下这里是不是真的更新
        SceneOperationDO tmp = sceneOperationDAO.updateSceneOperation(sceneOperationDO);
        if (tmp.getId().isEmpty()) {
            // 更新失败
            throw new BizException(ErrorCode.UNKNOWN_ERROR);
        }
        return true;
    }

    @Override
    public SceneOperationDTO getOperationsBySceneId(long sceneId) {
        SceneOperationDO sceneOperationDO = sceneOperationDAO.selectSceneOperationBySceneId(sceneId);
        SceneOperationDTO sceneOperationDTO = new SceneOperationDTO();
        BeanUtils.copyProperties(sceneOperationDO, sceneOperationDTO);
        return sceneOperationDTO;
    }

    @Override
    public List<String> getQrCodesBySceneId(long sceneId) {
        SceneOperationDTO sceneOperationDTO = getOperationsBySceneId(sceneId);
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
        sceneOperationDTO.getCommands().forEach(command -> {
            String component = command.getCommandComponent();
            String operation = command.getCommandOperation();
            ResultData resultData = machineControlClient.chooseComponent(command.getQrCode(), component, operation);
            if (!resultData.getResponseCode().equals(ResponseCode.RESPONSE_OK)) {
                // todo 打日志记录下来
                System.out.println("日志记录某设备操作失败");
            }
        });
    }

}
