package finley.gmair.scene.service.impl;

import com.google.common.collect.Lists;
import finley.gmair.scene.dao.SceneDAO;
import finley.gmair.scene.dto.SceneDTO;
import finley.gmair.scene.dto.SceneOperationDTO;
import finley.gmair.scene.entity.SceneDO;
import finley.gmair.scene.service.SceneOperationService;
import finley.gmair.scene.service.SceneService;
import finley.gmair.scene.utils.BizException;
import finley.gmair.scene.utils.ErrorCode;
import finley.gmair.scene.vo.ApiResult;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SceneServiceImpl implements SceneService {

    @Resource
    private SceneDAO sceneDAO;

    @Resource
    private SceneOperationService sceneOperationService;

    @Override
    public boolean createScene(SceneDTO sceneDTO) {
        SceneDO sceneDO = new SceneDO();
        BeanUtils.copyProperties(sceneDTO, sceneDO);

        long sceneId = sceneDAO.insertSceneDO(sceneDO);
        if (sceneId == 0) {
            // 场景创建失败
            throw new BizException(ErrorCode.UNKNOWN_ERROR);
        }
        sceneDTO.setId(sceneId);
        // 获取场景内的命令
        SceneOperationDTO sceneOperationDTO = sceneDTO.getSceneOperation();
        sceneOperationDTO.setSceneId(sceneId);
        boolean flag = sceneOperationService.createSceneOperation(sceneOperationDTO);
        if (!flag) {
            // 场景内操作写入失败
            throw new BizException(ErrorCode.UNKNOWN_ERROR);
        }
        return true;
    }

    @Override
    public boolean removeSceneBySceneId(long sceneId) {
        boolean flag = sceneDAO.deleteById(sceneId);
        if (!flag) {
            // 场景删除失败
            throw new BizException(ErrorCode.UNKNOWN_ERROR);
        }
        // todo 删除场景时是否需要删除场景内的命令（不删的理由，该部分数据可以作为数据分析）
        return true;
    }

    @Override
    public boolean removeScenesByConsumerId(String consumerId) {
        boolean flag = sceneDAO.deleteByConsumerId(consumerId);
        if (!flag) {
            // 场景删除失败
            throw new BizException(ErrorCode.UNKNOWN_ERROR);
        }
        // todo 删除场景时是否需要删除场景内的命令（不删的理由，该部分数据可以作为数据分析）
        return true;
    }

    @Override
    public boolean updateScene(SceneDTO sceneDTO) {
        SceneDO sceneDO = new SceneDO();
        BeanUtils.copyProperties(sceneDO, sceneDTO);
        boolean flag = sceneDAO.updateSceneDO(sceneDO);
        if (!flag) {
            // 场景更新失败
            throw new BizException(ErrorCode.UNKNOWN_ERROR);
        }

        // 更新场景内的操作
        SceneOperationDTO sceneOperationDTO = sceneDTO.getSceneOperation();
        flag = sceneOperationService.updateSceneOperation(sceneOperationDTO);
        if (!flag) {
            // 场景内操作更新失败
            throw new BizException(ErrorCode.UNKNOWN_ERROR);
        }
        return true;
    }

    @Override
    public List<SceneDTO> getScenesByConsumerId(String consumerId) {
        List<SceneDO> sceneDOS = sceneDAO.selectScenesByConsumerId(consumerId);
        // 获取场景内包含的操作
        List<SceneDTO> sceneDTOS = sceneDOS.stream().map(this::sceneDO2DTO).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(sceneDTOS)) {
            return Lists.newArrayList();
        }
        return sceneDTOS;
    }

    @Override
    public SceneDTO getSceneBySceneId(long sceneId) {
        SceneDO sceneDO = sceneDAO.selectSceneById(sceneId);
        return sceneDO2DTO(sceneDO);
    }

    /**
     * SceneDO -> SceneDTO
     * 需要经历的步骤：
     * 1。获取场景内包含的设备
     * 2。获取场景内操作
     * 3。获取场景内数据指标
     */
    public SceneDTO sceneDO2DTO(SceneDO sceneDO) {
        long sceneId = sceneDO.getId();
        SceneDTO sceneDTO = new SceneDTO();
        BeanUtils.copyProperties(sceneDO, sceneDTO);

        // 获取场景内操作
        SceneOperationDTO sceneOperationDTO = sceneOperationService.getOperationsBySceneId(sceneId);
        sceneDTO.setSceneOperation(sceneOperationDTO);
        // 获取场景内包含的设备
        List<String> qrCodes = sceneOperationService.getQrCodesBySceneId(sceneOperationDTO);
        sceneDTO.setQrCodes(qrCodes);
        // 获取场景内数据指标
        if (sceneDTO.getCo2() == 0 || sceneDTO.getHumidity() == 0 || sceneDTO.getPm25() == 0) {
            //todo 获取场景内数据指标
            double co2 = 1;
            double humidity = 1;
            double pm25 = 1;
            sceneDTO.setCo2(co2);
            sceneDTO.setHumidity(humidity);
            sceneDTO.setPm25(pm25);
        }
        return sceneDTO;
    }

    @Override
    public ApiResult startScene() {
        return null;
    }

    @Override
    public ApiResult stopScene() {
        return null;
    }
}
