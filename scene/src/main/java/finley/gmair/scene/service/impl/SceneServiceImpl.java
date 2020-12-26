package finley.gmair.scene.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import finley.gmair.scene.client.MachineClient;
import finley.gmair.scene.constant.ErrorCode;
import finley.gmair.scene.constant.OperateType;
import finley.gmair.scene.dao.SceneDAO;
import finley.gmair.scene.dto.SceneDTO;
import finley.gmair.scene.dto.SceneOperationDTO;
import finley.gmair.scene.entity.SceneDO;
import finley.gmair.scene.service.SceneOperationService;
import finley.gmair.scene.service.SceneService;
import finley.gmair.scene.utils.BizException;
import finley.gmair.scene.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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

    @Resource
    private RedisUtil redisUtil;

    @Override
    public SceneDTO createScene(SceneDTO sceneDTO) {
        log.info("sceneDTO is: {}", JSON.toJSONString(sceneDTO));
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

        // todo 删除场景时是否需要删除场景内的命令（不删的理由，该部分数据可以作为数据分析）
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
        //todo 调用频繁，考虑加一层缓存来优化
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
        SceneOperationDTO sceneOperationDTO = sceneOperationService.getOperationBySceneId(sceneId);
        sceneDTO.setSceneOperation(sceneOperationDTO);
        // 获取场景内包含的设备（从redis中获取）
        List<String> qrCodes = getSceneQrCodesBySceneId(sceneId);
        sceneDTO.setQrCodes(qrCodes);
        // 获取场景内数据指标
//        if (sceneDTO.getCo2() == 0 || sceneDTO.getHumidity() == 0 || sceneDTO.getPm25() == 0) {
//            //todo 获取场景内数据指标
//            double co2 = 0;
//            double humidity = 0;
//            double pm25 = 0;
//            double temperature = 0;
//            for (String qrCode : qrCodes) {
//                // 没有批量接口，只能for循环依次获取设备状态
//                ResultData data = machineClient.runningStatus(qrCode);
//                JSONObject object = JSON.parseObject(JSON.toJSONString(data.getData()));
//                co2 += object.getDoubleValue("co2");
//                humidity += object.getDoubleValue("humidity");
//                temperature += object.getDoubleValue("temperature");
//                pm25 += object.getDoubleValue("pm2_5");
//            }
//            sceneDTO.setTemperature(temperature / qrCodes.size());
//            sceneDTO.setCo2(co2 / qrCodes.size());
//            sceneDTO.setHumidity(humidity / qrCodes.size());
//            sceneDTO.setPm25(pm25 / qrCodes.size());
//        }
        return sceneDTO;
    }

    @Override
    public void startScene(long sceneId) {
        SceneOperationDTO sceneOperationDTO = sceneOperationService.getOperationBySceneId(sceneId);
        sceneOperationService.executeOperation(sceneOperationDTO);
    }

    @Override
    public void stopScene(long sceneId) {
        List<String> qrCodes = getSceneQrCodesBySceneId(sceneId);
        for (String qrcode : qrCodes) {
            // 关闭所有设备
            machineClient.chooseComponent(qrcode, OperateType.POWER.getOperate(), "off");
        }
    }

    @Override
    public List<String> getSceneQrCodesBySceneId(long sceneId) {

        // todo 考虑如何使用 redis 做缓存
        List<String> qrCodes;

        // 获取场景内操作
        SceneOperationDTO sceneOperationDTO = sceneOperationService.getOperationBySceneId(sceneId);
        // 获取场景内包含的设备
        qrCodes = sceneOperationService.getQrCodesBySceneId(sceneOperationDTO);
        if (CollectionUtils.isEmpty(qrCodes)) {
            return Lists.newArrayList();
        }

//        // 用redis存储这部分信息
//        List<Object> objects = redisUtil.lGet(Long.toString(sceneId), 0, -1);
//        if (CollectionUtils.isEmpty(objects)) {
//            // 获取场景内操作
//            SceneOperationDTO sceneOperationDTO = sceneOperationService.getOperationBySceneId(sceneId);
//            // 获取场景内包含的设备
//            qrCodes = sceneOperationService.getQrCodesBySceneId(sceneOperationDTO);
//            if (CollectionUtils.isEmpty(qrCodes)) {
//                return Lists.newArrayList();
//            }
//        } else {
//            qrCodes = objects.stream()
//                    .map(Objects::toString)
//                    .collect(Collectors.toList());
//        }
//        redisUtil.lSet(Long.toString(sceneId), qrCodes);
        return qrCodes;
    }
}
