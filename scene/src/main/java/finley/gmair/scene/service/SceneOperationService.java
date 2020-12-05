package finley.gmair.scene.service;

import finley.gmair.scene.dto.SceneOperationDTO;

import java.util.List;

/**
 * @author : Lyy
 * @create : 2020-12-04 17:36
 **/
public interface SceneOperationService {

    boolean createSceneOperation(SceneOperationDTO sceneOperationDTO);

    boolean updateSceneOperation(SceneOperationDTO sceneOperationDTO);

    SceneOperationDTO getOperationsBySceneId(long sceneId);

    List<String> getQrCodesBySceneId(long sceneId);

    List<String> getQrCodesBySceneId(SceneOperationDTO sceneOperationDTO);

    void executeOperation(SceneOperationDTO sceneOperationDTO);
}
