package finley.gmair.scene.dao.repository;

import finley.gmair.scene.entity.SceneOperationDO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author : Lyy
 * @create : 2020-12-05 14:34
 **/
@Repository
public interface SceneOperationRepository extends MongoRepository<SceneOperationDO, String> {
    SceneOperationDO findBySceneIdAndDeletedFalse(Long sceneId);

    // 根据用户ID获取场景控制信息
    List<SceneOperationDO> findAllByConsumerIdAndDeletedFalse(String consumerId);

    // 查询所有的场景控制记录
    List<SceneOperationDO> findAllByDeletedFalse();
}
