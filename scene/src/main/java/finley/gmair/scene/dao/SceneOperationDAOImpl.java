package finley.gmair.scene.dao;

import finley.gmair.scene.dao.repository.SceneOperationRepository;
import finley.gmair.scene.entity.SceneOperationDO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * @author : Lyy
 * @create : 2020-12-05 16:38
 **/
@Service
public class SceneOperationDAOImpl implements SceneOperationDAO {
    @Resource
    SceneOperationRepository sceneOperationRepository;

    @Override
    public SceneOperationDO insertSceneOperation(SceneOperationDO sceneOperationDO) {
        sceneOperationDO.setCreateTime(LocalDateTime.now());
        sceneOperationDO.setUpdateTime(LocalDateTime.now());
        return sceneOperationRepository.save(sceneOperationDO);
    }

    @Override
    public SceneOperationDO deleteSceneOperation() {
        return null;
    }

    @Override
    public SceneOperationDO updateSceneOperation(SceneOperationDO sceneOperationDO) {
        sceneOperationDO.setUpdateTime(LocalDateTime.now());
        return sceneOperationRepository.save(sceneOperationDO);
    }

    @Override
    public SceneOperationDO selectSceneOperationBySceneId(long sceneId) {
        return sceneOperationRepository.findBySceneId(sceneId);
    }
}
