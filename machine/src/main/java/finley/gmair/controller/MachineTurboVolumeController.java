package finley.gmair.controller;

import finley.gmair.model.machine.MachineTurboVolume;
import finley.gmair.service.MachineTurboVolumeService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Bright Chan
 * @date: 2020/7/18 15:09
 * @description: TODO
 */

@RestController
@RequestMapping("/machine/turboVolume")
public class MachineTurboVolumeController {

    @Autowired
    private MachineTurboVolumeService machineTurboVolumeService;

    /**
     * 查询设备隐藏风量开关状态，如果返回码为ERROR，则表示该设备没有隐藏风量选项
     * @param qrcode 设备二维码
     * @return 查询结果
     */
    @GetMapping("/getStatus")
    public ResultData getTurboVolumeStatus(@RequestParam String qrcode) {
        ResultData res = new ResultData();
        Map<String, Object> resData = new HashMap<>();

        ResultData response = machineTurboVolumeService.fetchByQRCode(qrcode);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            res.setResponseCode(ResponseCode.RESPONSE_ERROR);
            res.setDescription("this machine does not have turbo volume");
            return res;
        }

        MachineTurboVolume selectedOne = (MachineTurboVolume) response.getData();
        resData.put("qrcode", qrcode);
        resData.put("turboVolumeStatus", selectedOne.isTurboVolumeStatus());
        res.setData(resData);
        return res;
    }

    /**
     * 改变设备隐藏风量开关状态
     * @param qrcode 设备二维码
     * @param turboVolumeStatus 修改值
     * @return 修改结果
     */
    @GetMapping("/changeStatus")
    public ResultData changeTurboVolumeStatus(@RequestParam String qrcode,
                                              @RequestParam boolean turboVolumeStatus) {
        ResultData res = new ResultData();
        Map<String, Object> resData = new HashMap<>();
        resData.put("qrcode", qrcode);

        //检测参数
        if (StringUtils.isEmpty(qrcode)) {
            res.setResponseCode(ResponseCode.RESPONSE_ERROR);
            res.setDescription("qrcode cannot be empty");
            return res;
        }

        Map<String, Object> condition = new HashMap<>();
        condition.put("qrcode", qrcode);
        condition.put("turboVolumeStatus", turboVolumeStatus);
        ResultData response = machineTurboVolumeService.modify(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            res.setResponseCode(ResponseCode.RESPONSE_ERROR);
            res.setDescription("modify machineTurboVolume failed");
            return res;
        }

        res.setData(resData);
        return res;
    }

    @GetMapping("/getValue")
    public ResultData showTurboVolumeValue(@RequestParam String qrcode) {
        ResultData res = new ResultData();

        //检测参数
        if (StringUtils.isEmpty(qrcode)) {
            res.setResponseCode(ResponseCode.RESPONSE_ERROR);
            res.setDescription("qrcode cannot be empty");
            return res;
        }
        ResultData response = machineTurboVolumeService.getTurboVolumeValue(qrcode);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            res.setResponseCode(ResponseCode.RESPONSE_ERROR);
            res.setDescription("get turbo volume failed");
            return res;
        }

        res.setData(response.getData());
        return res;
    }
}
