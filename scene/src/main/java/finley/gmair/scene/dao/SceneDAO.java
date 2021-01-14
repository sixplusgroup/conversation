package finley.gmair.scene.dao;

import finley.gmair.scene.entity.SceneDO;

import java.util.List;

/**
 * @author : Lyy
 * @create : 2020-12-05 15:56
 * @description scene表数据访问层（mysql）
 **/
public interface SceneDAO {
    long insertSceneDO(SceneDO sceneDO);

    boolean deleteById(long sceneId);

    boolean deleteByConsumerId(String consumerId);

    boolean updateSceneDO(SceneDO sceneDO);

    SceneDO selectSceneById(long sceneId);

    SceneDO selectSceneByName(String sceneName);

    List<SceneDO> selectScenesByConsumerId(String consumerId);
}
