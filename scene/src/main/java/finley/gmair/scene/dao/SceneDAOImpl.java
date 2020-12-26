package finley.gmair.scene.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import finley.gmair.scene.dao.repository.SceneMapper;
import finley.gmair.scene.entity.SceneDO;
import finley.gmair.scene.utils.RedisUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author : Lyy
 * @create : 2020-12-05 18:31
 **/
@Service
public class SceneDAOImpl implements SceneDAO {

    @Resource
    private SceneMapper sceneMapper;

    @Resource
    private RedisUtil redisUtil;

    @Override
    public long insertSceneDO(SceneDO sceneDO) {
        int row = sceneMapper.insert(sceneDO);
        if (row != 1) {
            // todo 日志记录数据插入失败
            return 0;
        }

        return sceneDO.getId();
    }

    @Override
    public boolean deleteById(long sceneId) {
        QueryWrapper<SceneDO> wrapper = new QueryWrapper<>();
        wrapper.eq("scene_id", sceneId);
        int row = sceneMapper.delete(wrapper);
        return row == 1;
    }

    @Override
    public boolean deleteByConsumerId(String consumerId) {
        QueryWrapper<SceneDO> wrapper = new QueryWrapper<>();
        wrapper.eq("consumer_id", consumerId);
        int row = sceneMapper.delete(wrapper);
        return row == 1;
    }

    @Override
    public boolean updateSceneDO(SceneDO sceneDO) {
        int row = sceneMapper.updateById(sceneDO);
        return row == 1;
    }

    @Override
    public SceneDO selectSceneById(long sceneId) {
        QueryWrapper<SceneDO> wrapper = new QueryWrapper<>();
        wrapper.eq("scene_id", sceneId);
        return sceneMapper.selectOne(wrapper);
    }

    @Override
    public List<SceneDO> selectScenesByConsumerId(String consumerId) {
        QueryWrapper<SceneDO> wrapper = new QueryWrapper<>();
        wrapper.eq("consumer_id", consumerId);
        return sceneMapper.selectList(wrapper);
    }
}
