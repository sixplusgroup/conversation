package finley.gmair.scene.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import finley.gmair.scene.client.MachineClient;
import finley.gmair.scene.constant.ErrorCode;
import finley.gmair.scene.dao.SceneDAO;
import finley.gmair.scene.dto.SceneDTO;
import finley.gmair.scene.dto.SceneOperationDTO;
import finley.gmair.scene.entity.SceneDO;
import finley.gmair.scene.service.SceneOperationService;
import finley.gmair.scene.service.SceneService;
import finley.gmair.scene.utils.BizException;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SceneServiceImpl implements SceneService {

    @Resource
    private SceneDAO sceneDAO;

    @Resource
    private SceneOperationService sceneOperationService;

    @Resource
    private MachineClient machineClient;

    @Override
    public SceneDTO createScene(SceneDTO sceneDTO) {

        SceneDO tmp = sceneDAO.selectSceneByName(sceneDTO.getName());
        if (ObjectUtils.isNotEmpty(tmp)) {
            // 场景名重复
            log.error("scene name duplicate，sceneDO is: {}", JSON.toJSONString(sceneDTO));
            throw new BizException(ErrorCode.SCENE_NAME_DUPLICATE);
        }

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
        sceneDTO.setSceneOperation(sceneOperationDTO);

        return sceneDTO;
    }

    @Override
    public SceneDTO updateScene(SceneDTO sceneDTO) {
        SceneDO sceneDO = new SceneDO();
        BeanUtils.copyProperties(sceneDTO, sceneDO);
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
        sceneDTO.setSceneOperation(sceneOperationDTO);
        return sceneDTO;
    }

    @Override
    public boolean removeSceneBySceneId(long sceneId) {
        boolean flag = sceneDAO.deleteById(sceneId);
        if (!flag) {
            // 场景删除失败
            throw new BizException(ErrorCode.UNKNOWN_ERROR);
        }
        flag = sceneOperationService.deleteSceneOperationBySceneId(sceneId);
        if (!flag) {
            // 场景删除失败
            throw new BizException(ErrorCode.UNKNOWN_ERROR);
        }
        return true;
    }

    @Override
    public boolean removeScenesByConsumerId(String consumerId) {
        boolean flag = sceneDAO.deleteByConsumerId(consumerId);
        if (!flag) {
            // 场景删除失败
            throw new BizException(ErrorCode.UNKNOWN_ERROR);
        }
        return true;
    }

    /**
     * 通过用户ID获取当前用户设置的所有场景
     *
     * @param consumerId 用户ID
     * @return List<SceneDTO>
     */
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
     * SceneDO -> SceneDTO 需要经历的步骤： 1。获取场景内包含的设备 2。获取场景内操作 3。获取场景内数据指标
     */
    public SceneDTO sceneDO2DTO(SceneDO sceneDO) {
        long sceneId = sceneDO.getId();
        SceneDTO sceneDTO = new SceneDTO();
        BeanUtils.copyProperties(sceneDO, sceneDTO);

        // 获取场景内操作
        SceneOperationDTO sceneOperationDTO = sceneOperationService.getOperationBySceneId(sceneId);
        sceneDTO.setSceneOperation(sceneOperationDTO);
        // 获取场景内包含的设备（从redis中获取）
        List<String> qrCodes = getSceneQrCodesBySceneId(sceneId);
        sceneDTO.setQrCodes(qrCodes);
        setData2Scene(sceneDTO, qrCodes);
        return sceneDTO;
    }

    /**
     * 获取场景内数据指标 以场景内所有机器相关数值的最大值作为指标 之后可以考虑增加衡量维度
     */
    public void setData2Scene(SceneDTO sceneDTO, List<String> qrCodes) {
        double co2 = 0;
        double humidity = 0;
        double pm25 = 0;
        double temperature = 0;
        for (String qrCode : qrCodes) {
            // 没有批量接口，只能for循环依次获取设备状态
            ResultData data = machineClient.runningStatus(qrCode);
            if (data.getResponseCode() == ResponseCode.RESPONSE_OK) {
                JSONObject object = JSON.parseObject(JSON.toJSONString(data.getData()));
                log.info("json data is:{}", JSON.toJSONString(object));
                co2 = Math.max(co2, object.getDoubleValue("co2"));
                humidity = Math.max(humidity, object.getDoubleValue("humidity"));
                temperature = Math.max(temperature, object.getDoubleValue("temperature"));
                pm25 = Math.max(pm25, object.getDoubleValue("pm2_5"));
            }
        }
        sceneDTO.setTemperature(temperature);
        sceneDTO.setCo2(co2);
        sceneDTO.setHumidity(humidity);
        sceneDTO.setPm25(pm25);
    }

    @Override
    public List<String> getSceneQrCodesBySceneId(long sceneId) {

        List<String> qrCodes;

        // 获取场景内操作
        SceneOperationDTO sceneOperationDTO = sceneOperationService.getOperationBySceneId(sceneId);
        // 获取场景内包含的设备
        qrCodes = sceneOperationService.getQrCodesBySceneId(sceneOperationDTO);
        log.info("qrCodes is:{}", JSON.toJSONString(qrCodes));
        if (CollectionUtils.isEmpty(qrCodes)) {
            return Lists.newArrayList();
        }
        return qrCodes;
    }
}