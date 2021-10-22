package finley.gmair.scene.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import finley.gmair.scene.dao.repository.SceneCommandMapper;
import finley.gmair.scene.entity.SceneCommandDO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author : Lyy
 * @create : 2020-12-26 17:18
 **/
@Service
public class SceneCommandDAOImpl implements SceneCommandDAO {

    @Resource
    SceneCommandMapper sceneCommandMapper;

    @Override
    public List<SceneCommandDO> getSceneCommandByGoodsID(String goodsId) {
        QueryWrapper<SceneCommandDO> wrapper = new QueryWrapper<>();
        wrapper.eq("goods_id", goodsId).eq("enable", true);
        return sceneCommandMapper.selectList(wrapper);
    }
}
