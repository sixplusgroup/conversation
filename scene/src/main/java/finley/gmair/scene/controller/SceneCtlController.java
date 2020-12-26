package finley.gmair.scene.controller;

import com.google.common.collect.Lists;
import finley.gmair.scene.dto.SceneDeviceControlOptionDTO;
import finley.gmair.scene.service.SceneControlService;
import finley.gmair.scene.utils.ResultUtil;
import finley.gmair.scene.vo.ApiResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author : Lyy
 * @create : 2020-12-26 16:06
 * @description 场景控制接口
 **/
@RestController()
@RequestMapping("/scene/control")
public class SceneCtlController {

    @Resource
    SceneControlService sceneControlService;

    @GetMapping("/options")
    public ApiResult getSceneDeviceControlOption(String consumerId) {
        List<SceneDeviceControlOptionDTO> sceneDeviceControlOptionList = sceneControlService.getSceneDeviceControlOptions(consumerId);
        return ResultUtil.success("场景控制列表获取成功", sceneDeviceControlOptionList);
    }

}
