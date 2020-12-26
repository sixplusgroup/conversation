package finley.gmair.scene.dao;

import finley.gmair.scene.entity.SceneOperationDO;

/**
 * @author : Lyy
 * @create : 2020-12-05 16:37
 * @description scene_operation 集合数据访问层（mongodb）
 **/
public interface SceneOperationDAO {
    SceneOperationDO insertSceneOperation(SceneOperationDO sceneOperationDO);

    SceneOperationDO deleteSceneOperation(long sceneId);

    int deleteSceneOperationsByConsumerId(String consumerId);

    SceneOperationDO updateSceneOperation(SceneOperationDO sceneOperationDO);

    SceneOperationDO selectSceneOperationBySceneId(long sceneId);
}
