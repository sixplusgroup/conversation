package finley.gmair.scene.controller;

import finley.gmair.scene.client.LogClient;
import finley.gmair.scene.client.MachineClient;
import finley.gmair.scene.service.LogService;
import finley.gmair.scene.utils.ResultUtil;
import finley.gmair.scene.vo.ApiResult;
import finley.gmair.util.ResultData;
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

    @GetMapping("/user/{uid}")
    public ApiResult getUserLog(@PathVariable(value = "uid") String uid) {
        return ResultUtil.success("用户日志获取成功", logService.queryLogByUid(uid));
    }

    @GetMapping("/user/devices/list/{consumerId}")
    public ApiResult getUserDevices(@PathVariable("consumerId")String consumerId){
        return ResultUtil.success("success",machineClient.obtainMachineList(consumerId));
    }
}
