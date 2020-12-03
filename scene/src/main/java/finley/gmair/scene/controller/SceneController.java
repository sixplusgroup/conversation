package finley.gmair.scene.controller;

import finley.gmair.scene.service.SceneService;
import finley.gmair.scene.vo.ApiResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/scene")
public class SceneController {

    @Resource
    SceneService sceneService;

    @PostMapping("/")
    public ApiResult createScene() {
        return sceneService.addScene();
    }

    @GetMapping("/list")
    public ApiResult getScene(long consumerId) {
        return sceneService.getScenesByConsumerId(consumerId);
    }



}
