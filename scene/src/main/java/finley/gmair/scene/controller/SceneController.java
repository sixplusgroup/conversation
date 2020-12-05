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
        boolean flag = sceneService.createScene(sceneDTO);
        return ResultUtil.success();
    }

    // 前端传json
    @PostMapping("/update")
    public ApiResult updateScene(@RequestBody SceneDTO sceneDTO) {
        boolean flag = sceneService.updateScene(sceneDTO);
        return ResultUtil.success();
    }

    @GetMapping("/list/cid")
    public ApiResult getScenesByConsumerId(String consumerId) {
        List<SceneDTO> result = sceneService.getScenesByConsumerId(consumerId);
        return ResultUtil.success("场景查询成功", result);
    }

    @GetMapping("/sid")
    public ApiResult getSceneBySceneId(long sceneId) {
        SceneDTO sceneVO = sceneService.getSceneBySceneId(sceneId);
        return ResultUtil.success("场景查询成功", sceneVO);
    }

}
