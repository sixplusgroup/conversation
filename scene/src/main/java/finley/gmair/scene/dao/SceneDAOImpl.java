package finley.gmair.scene.dao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import finley.gmair.scene.dao.repository.SceneMapper;
import finley.gmair.scene.entity.SceneDO;
import finley.gmair.scene.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author : Lyy
 * @create : 2020-12-05 18:31
 **/
@Service
@Slf4j
public class SceneDAOImpl implements SceneDAO {

    // 缓存存活时间
    private final int time = 60;

    @Resource
    private SceneMapper sceneMapper;

    @Resource
    private RedisUtil redisUtil;

    @Override
    public long insertSceneDO(SceneDO sceneDO) {
        int row = sceneMapper.insert(sceneDO);
        if (row != 1) {
            log.error("insert scene failed，sceneDO is: {}", JSON.toJSONString(sceneDO));
            return 0;
        }
        // 在缓存中删除scene
        redisUtil.del(sceneDO.getConsumerId());
        return sceneDO.getId();
    }

    @Override
    public boolean deleteById(long sceneId) {

        // 在缓存中删除scene
        if (redisUtil.hasKey(Long.toString(sceneId))) {
            redisUtil.del(Long.toString(sceneId));
        }

        SceneDO sceneDO = selectSceneById(sceneId);
        if (redisUtil.hasKey(sceneDO.getConsumerId())) {
            redisUtil.del(sceneDO.getConsumerId());
        }

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

        // 查找consumerId下所拥有的场景ID，一并删除
        List<SceneDO> sceneDOS = selectScenesByConsumerId(consumerId);
        redisUtil.del(sceneDOS.stream().map(sceneDO -> Long.toString(sceneDO.getId())).toArray(String[]::new));
        redisUtil.del(consumerId);
        return row == 1;
    }

    @Override
    public boolean updateSceneDO(SceneDO sceneDO) {
        int row = sceneMapper.updateById(sceneDO);

        // 更新时删除当前sceneDO的缓存和当前用户的sceneDO缓存
        redisUtil.del(Long.toString(sceneDO.getId()), sceneDO.getConsumerId());
        return row == 1;
    }

    @Override
    public SceneDO selectSceneById(long sceneId) {
        // 先查redis，如果有返回，如果没有查数据库
        SceneDO sceneDO = (SceneDO) redisUtil.get(Long.toString(sceneId));
        if (ObjectUtils.isNotEmpty(sceneDO)) {
            log.info("result from redis，sceneDO is:{}", JSON.toJSONString(sceneDO));
            return sceneDO;
        }
        QueryWrapper<SceneDO> wrapper = new QueryWrapper<>();
        wrapper.eq("scene_id", sceneId);

        SceneDO scene = sceneMapper.selectOne(wrapper);
        if (ObjectUtils.isNotEmpty(scene)) {
            // 查询成功，写入redis，时间为60s
            redisUtil.set(Long.toString(sceneId), scene, time);
        }
        return scene;
    }

    @Override
    public List<SceneDO> selectScenesByConsumerId(String consumerId) {
        // 先查redis
        Object obj = redisUtil.get(consumerId);

        if (ObjectUtils.isNotEmpty(obj)) {
            log.info("result from redis，sceneDO is:{}", JSON.toJSONString(obj));
            List<SceneDO> resFormRedis = JSONArray.parseArray(obj.toString(), SceneDO.class);

            if (CollectionUtils.isNotEmpty(resFormRedis)) {
                return resFormRedis;
            }
        }
        QueryWrapper<SceneDO> wrapper = new QueryWrapper<>();
        wrapper.eq("consumer_id", consumerId);
        List<SceneDO> sceneDOS = sceneMapper.selectList(wrapper);
        if (CollectionUtils.isNotEmpty(sceneDOS)) {
            // 查到结果后，保存到redis中
            redisUtil.set(consumerId, sceneDOS, time);
        }
        return sceneDOS;
    }
}
