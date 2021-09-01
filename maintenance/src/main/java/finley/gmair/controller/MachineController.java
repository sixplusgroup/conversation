package finley.gmair.controller;

import finley.gmair.service.MachineService;
import finley.gmair.util.ResultData;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 设备信息相关的交互接口
 *
 * @author lycheeshell
 * @date 2021/1/17 14:39
 */
@RestController
@RequestMapping("/maintenance/machine")
public class MachineController {

    @Resource
    private MachineService machineService;

    /**
     * 查询设备最新的状态信息
     *
     * @param qrcode 二维码
     * @return 设备最新的状态信息
     */
    @GetMapping(value = "/getLatestStatus")
    public ResultData getLatestStatus(String qrcode) {
        if (StringUtils.isEmpty(qrcode)) {
            return ResultData.error("qrcode为空");
        }

        return machineService.runningStatus(qrcode);
    }

    /**
     * 查询设备的定时配置
     *
     * @param qrcode 二维码
     * @return 设备定时数据
     */
    @GetMapping(value = "/getTimingSetting")
    public ResultData getTimingSetting(String qrcode) {
        if (StringUtils.isEmpty(qrcode)) {
            return ResultData.error("qrcode为空");
        }

        return machineService.getRecord(qrcode);
    }

}
