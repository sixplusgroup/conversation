package finley.gmair.scene.controller;

import finley.gmair.scene.dto.SceneDTO;
import finley.gmair.scene.service.SceneService;
import finley.gmair.scene.utils.ResultUtil;
import finley.gmair.scene.vo.ApiResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/scene")
public class SceneController {

    @Resource
    SceneService sceneService;

    // 前端传json
    @PostMapping("/create")
    public ApiResult createScene(@RequestBody SceneDTO sceneDTO) {
        SceneDTO scene = sceneService.createScene(sceneDTO);
        return ResultUtil.success("创建成功", scene);
    }

    // 前端传json
    @PostMapping("/update")
    public ApiResult updateScene(@RequestBody SceneDTO sceneDTO) {
        SceneDTO scene = sceneService.updateScene(sceneDTO);
        return ResultUtil.success("更新成功", scene);
    }

    // 根据用户ID获取该用户所包含的全部场景信息
    @GetMapping("/list/cid")
    public ApiResult getScenesByConsumerId(String consumerId) {
        List<SceneDTO> result = sceneService.getScenesByConsumerId(consumerId);
        return ResultUtil.success("场景查询成功", result);
    }

    // 根据场景ID获取场景信息
    @GetMapping("/{sid}")
    public ApiResult getSceneBySid(@PathVariable(value = "sid") long sceneId) {
        SceneDTO sceneDTO = sceneService.getSceneBySceneId(sceneId);
        return ResultUtil.success("场景获取成功", sceneDTO);
    }

    @GetMapping("/sid")
    public ApiResult getSceneBySceneId(long sceneId) {
        SceneDTO sceneVO = sceneService.getSceneBySceneId(sceneId);
        return ResultUtil.success("场景查询成功", sceneVO);
    }

}
