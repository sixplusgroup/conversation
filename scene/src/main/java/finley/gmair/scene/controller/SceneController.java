package finley.gmair.scene.controller;

import finley.gmair.scene.dto.SceneDTO;
import finley.gmair.scene.service.SceneService;
import finley.gmair.scene.utils.ResultUtil;
import finley.gmair.scene.vo.ApiResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


/**
 * @author : Lyy
 * @create : 2020-12-03 16:06
 * @description 场景接口
 **/
@RestController
@RequestMapping("/scene")
public class SceneController {

    @Resource
    SceneService sceneService;

    /**
     * 创建场景
     *
     * @param sceneDTO 前端传json
     * @return sceneDTO
     */
    @PostMapping("/create")
    public ApiResult createScene(@RequestBody SceneDTO sceneDTO) {
        SceneDTO scene = sceneService.createScene(sceneDTO);
        return ResultUtil.success("创建成功", scene);
    }

    /**
     * 更新场景
     *
     * @param sceneDTO 前端传json
     * @return sceneDTO
     */
    @PostMapping("/update")
    public ApiResult updateScene(@RequestBody SceneDTO sceneDTO) {
        SceneDTO scene = sceneService.updateScene(sceneDTO);
        return ResultUtil.success("更新成功", scene);
    }

    /**
     * 根据用户ID获取该用户所包含的全部场景信息
     *
     * @param consumerId 用户ID
     * @return List<SceneDTO>
     */
    @GetMapping("/list/cid")
    public ApiResult getScenesByConsumerId(String consumerId) {
        List<SceneDTO> result = sceneService.getScenesByConsumerId(consumerId);
        return ResultUtil.success("场景查询成功", result);
    }

    /**
     * 根据场景ID获取场景信息
     *
     * @param sid 场景ID
     * @return sceneDTO
     */
    @GetMapping("/")
    public ApiResult getSceneBySid(long sid) {
        SceneDTO sceneDTO = sceneService.getSceneBySceneId(sid);
        return ResultUtil.success("场景获取成功", sceneDTO);
    }

    @PostMapping("/delete")
    public ApiResult deleteSceneBySid(long sceneId) {
        return ResultUtil.success("场景删除成功", sceneService.removeSceneBySceneId(sceneId));
    }

}
