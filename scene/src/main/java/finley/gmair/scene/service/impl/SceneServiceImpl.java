package finley.gmair.scene.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import finley.gmair.scene.entity.SceneDO;
import finley.gmair.scene.mapper.SceneMapper;
import finley.gmair.scene.service.SceneService;
import finley.gmair.scene.utils.ResultUtil;
import finley.gmair.scene.vo.ApiResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SceneServiceImpl implements SceneService {

    @Resource
    private SceneMapper sceneMapper;

    @Override
    public ApiResult addScene() {
        return null;
    }

    @Override
    public ApiResult removeScene() {
        return null;
    }

    @Override
    public ApiResult updateScene() {
        return null;
    }

    @Override
    public ApiResult getScenesByConsumerId(long consumerId) {
        QueryWrapper<SceneDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("consumer_id", consumerId);
        List<SceneDO> sceneDOS = sceneMapper.selectList(queryWrapper);
        return ResultUtil.success("场景查询成功", sceneDOS);
    }

    @Override
    public ApiResult getSceneById(long sceneId) {
        return null;
    }

    @Override
    public ApiResult startScene() {
        return null;
    }

    @Override
    public ApiResult stopScene() {
        return null;
    }
}
