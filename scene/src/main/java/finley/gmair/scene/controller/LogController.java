package finley.gmair.scene.controller;

import finley.gmair.scene.client.MachineClient;
import finley.gmair.scene.service.LogService;
import finley.gmair.scene.utils.ResultUtil;
import finley.gmair.scene.vo.ApiResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author : Lyy
 * @create : 2021-01-14 20:30
 **/
@RestController
@RequestMapping("/scene/log")
public class LogController {

    @Resource
    LogService logService;

    @Resource
    MachineClient machineClient;

    @GetMapping("/machine/com/{uid}")
    public ApiResult queryMachineComLog(@PathVariable(value = "uid") String uid) {
        return ResultUtil.success("用户日志获取成功", logService.queryMachineComLog(uid));
    }

    @GetMapping("/user/{uid}")
    public ApiResult queryUserLog(@PathVariable(value = "uid") String uid) {
        return ResultUtil.success("用户日志获取成功", logService.queryUserLog(uid));
    }

    // todo 测试用接口，之后删除
    @GetMapping("/user/devices/list/{consumerId}")
    public ApiResult getUserDevices(@PathVariable("consumerId") String consumerId) {
        return ResultUtil.success("success", machineClient.obtainMachineList(consumerId));
    }
}
