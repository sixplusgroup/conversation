package finley.gmair.scene.dao;

import finley.gmair.scene.entity.SceneCommandDO;

import java.util.List;

/**
 * @author : Lyy
 * @create : 2020-12-26 17:18
 **/
public interface SceneCommandDAO {
    List<SceneCommandDO> getSceneCommandByGoodsID(String goodsId);
}
