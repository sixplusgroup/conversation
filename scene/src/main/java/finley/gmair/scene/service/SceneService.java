package finley.gmair.scene.service;

import finley.gmair.scene.vo.ApiResult;

public interface SceneService {

    // 添加场景
    ApiResult addScene();

    // 移除场景
    ApiResult removeScene();

    // 更新场景
    ApiResult updateScene();

    // 根据用户id获取所有场景
    ApiResult getScenesByConsumerId(long consumerId);

    ApiResult getSceneById(long sceneId);

    // 启动场景（运行场景内的所有设备）
    ApiResult startScene();

    // 结束场景（关闭场景内的所有设备）
    ApiResult stopScene();
}
