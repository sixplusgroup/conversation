package finley.gmair.scene.service;

import finley.gmair.scene.dto.SceneDTO;

import java.util.List;

public interface SceneService {

    // 添加场景
    SceneDTO createScene(SceneDTO sceneDTO);

    // 根据ID移除场景
    boolean removeSceneBySceneId(long sceneId);

    // 根据用户ID移除场景
    boolean removeScenesByConsumerId(String consumerId);

    // 更新场景
    SceneDTO updateScene(SceneDTO sceneDTO);

    // 根据用户id获取所有场景
    List<SceneDTO> getScenesByConsumerId(String consumerId);

    // 根据场景ID 获取场景信息
    SceneDTO getSceneBySceneId(long sceneId);

    // 根据场景ID获取场景内的所有设备二维码
    List<String> getSceneQrCodesBySceneId(long sceneId);
}
