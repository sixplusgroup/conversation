package finley.gmair.scene.controller;

import finley.gmair.scene.service.LogService;
import finley.gmair.scene.utils.ResultUtil;
import finley.gmair.scene.vo.ApiResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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


    /**
     * 获取用户控制设备的日志
     *
     * @param uid    用户ID（非必需，如果不填，则查询设备的全部日志）
     * @param qrCode 设备二维码 （非必需，如果不填，则查询用户的全部控制日志）
     * @return 结果
     */
    @GetMapping("/user/action")
    public ApiResult getUserActionLog(@RequestParam(value = "uid") String uid, @RequestParam(value = "qrcode") String qrCode) {
        return ResultUtil.success("用户日志获取成功", logService.getUserActionLog(uid, qrCode).getData());
    }
}
