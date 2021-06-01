package finley.gmair.scene.controller;

import finley.gmair.scene.dto.AppMsgPushDTO;
import finley.gmair.scene.service.AppMsgPushService;
import finley.gmair.scene.utils.ResultUtil;
import finley.gmair.scene.vo.ApiResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author : Lyy
 * @create : 2021-01-15 14:31
 * @description 消息推送接口（推送到App，包括安卓和iOS）
 **/
@RestController
@RequestMapping("/scene/msg/push")
public class MsgPushController {

    @Resource
    AppMsgPushService msgPushService;

    @PostMapping("/")
    public ApiResult sendAppMsg(@RequestBody AppMsgPushDTO appMsgPush) {
        boolean flag = msgPushService.sendPush(appMsgPush);
        if (!flag) {
            return ResultUtil.error("App通知发送失败");
        }
        return ResultUtil.success("App通知发送成功");
    }
}
