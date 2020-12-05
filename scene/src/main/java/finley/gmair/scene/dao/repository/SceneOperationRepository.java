package finley.gmair.scene.dao.repository;

import finley.gmair.scene.entity.SceneOperationDO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author : Lyy
 * @create : 2020-12-05 14:34
 **/
@Repository
public interface SceneOperationRepository extends MongoRepository<SceneOperationDO, String> {
    SceneOperationDO findBySceneId(Long sceneId);
}
