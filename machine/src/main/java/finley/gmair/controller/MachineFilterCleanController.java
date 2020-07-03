package finley.gmair.controller;

import finley.gmair.util.ResultData;
import org.springframework.web.bind.annotation.*;

/**
 * @author: Bright Chan
 * @date: 2020/7/3 10:54
 * @description: TODO
 */

@RestController
@RequestMapping("/machine/filter/clean")
public class MachineFilterCleanController {

    /**
     * 查询设备初效滤网是否需要清洗
     * @return ResultData，若返回成功，则data字段中包含qrcode、lightStatus和createAt三个属性。
     */
    @GetMapping("/")
    public ResultData filterNeedCleanOrNot(@RequestParam("appid") String appId,
                                           @RequestParam String qrcode) {
        ResultData res = new ResultData();
        return res;
    }

    /**
     * 查询设备初效滤网清洗提醒是否开启
     * @return ResultData，若返回成功，则data字段中包含qrcode、isOpen和createAt三个属性。
     */
    @GetMapping("/isOpen")
    public ResultData filterCleanRemindIsOpen(@RequestParam("appid") String appId,
                                              @RequestParam String qrcode) {
        ResultData res = new ResultData();
        return res;
    }

    /**
     * 改变设备初效滤网清洗提醒开启状态
     * @return ResultData，若返回成功，则data字段中包含qrcode和createAt两个属性。
     */
    @PostMapping("/change")
    public ResultData changeFilterCleanRemindStatus(@RequestParam("appid") String appId,
                                                    @RequestParam String qrcode,
                                                    @RequestParam String status) {
        ResultData res = new ResultData();
        return res;
    }

    /**
     * 确认清洗
     * @return ResultData，若返回成功，则data字段中包含qrcode和createAt两个属性。
     */
    @GetMapping("/confirm")
    public ResultData confirmClean(@RequestParam("appid") String appId,
                                   @RequestParam String qrcode) {
        ResultData res = new ResultData();
        return res;
    }
}
