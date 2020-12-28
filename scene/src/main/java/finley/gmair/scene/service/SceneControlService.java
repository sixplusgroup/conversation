package finley.gmair.scene.service;

import finley.gmair.scene.dto.SceneDeviceControlOptionDTO;

import java.util.List;

/**
 * @author : Lyy
 * @create : 2020-12-26 16:10
 * @description 场景控制接口
 **/
public interface SceneControlService {
    List<SceneDeviceControlOptionDTO> getSceneDeviceControlOptions(String consumerId);

    // 启动场景（运行场景内的所有设备）
    void startScene(long sceneId);

    // 结束场景（关闭场景内的所有设备）
    void stopScene(long sceneId);
}
