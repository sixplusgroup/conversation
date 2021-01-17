package finley.gmair.controller;

import finley.gmair.util.ResultData;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 设备信息相关的接口
 *
 * @author lycheeshell
 * @date 2021/1/17 14:39
 */
@RestController
@RequestMapping("/maintenance/machine")
public class MachineController {

    /**
     * 查询设备最新的状态信息
     *
     * @param qrcode 二维码
     * @return 设备最新的状态信息
     */
    @GetMapping(value = "/getLatestStatus")
    public ResultData getLatestStatus(String qrcode) {


        return ResultData.ok(null);
    }

}
