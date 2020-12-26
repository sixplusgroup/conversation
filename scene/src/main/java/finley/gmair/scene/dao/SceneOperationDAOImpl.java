package finley.gmair.scene.dao;

import finley.gmair.scene.dao.repository.SceneOperationRepository;
import finley.gmair.scene.entity.SceneOperationDO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

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
        sceneOperationDO.setDeleted(false);
        sceneOperationDO.setCreateTime(LocalDateTime.now());
        sceneOperationDO.setUpdateTime(LocalDateTime.now());
        return sceneOperationRepository.save(sceneOperationDO);
    }

    // 逻辑删除
    @Override
    public SceneOperationDO deleteSceneOperation(long sceneId) {
        SceneOperationDO sceneOperationDO = selectSceneOperationBySceneId(sceneId);
        sceneOperationDO.setDeleted(true);
        return updateSceneOperation(sceneOperationDO);
    }

    // 批量逻辑删除
    @Override
    public int deleteSceneOperationsByConsumerId(String consumerId) {
        List<SceneOperationDO> sceneOperationDOS = sceneOperationRepository.findAllByConsumerIdAndDeletedFalse(consumerId);
        for (SceneOperationDO operationDO : sceneOperationDOS) {
            operationDO.setDeleted(true);
            operationDO.setUpdateTime(LocalDateTime.now());
        }
        sceneOperationRepository.save(sceneOperationDOS);
        return sceneOperationDOS.size();
    }

    @Override
    public SceneOperationDO updateSceneOperation(SceneOperationDO sceneOperationDO) {
        sceneOperationDO.setUpdateTime(LocalDateTime.now());
        return sceneOperationRepository.save(sceneOperationDO);
    }

    @Override
    public SceneOperationDO selectSceneOperationBySceneId(long sceneId) {
        return sceneOperationRepository.findBySceneIdAndDeletedFalse(sceneId);
    }
}
