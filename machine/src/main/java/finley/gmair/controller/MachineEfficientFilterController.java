package finley.gmair.controller;

import finley.gmair.model.machine.EfficientFilterRemindStatus;
import finley.gmair.model.machine.EfficientFilterStatus;
import finley.gmair.model.machine.MachineEfficientFilter;
import finley.gmair.service.MachineEfficientFilterService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Bright Chan
 * @date: 2020/7/24 16:34
 * @description: 设备高效滤网控制类
 */

@RestController
@RequestMapping("/machine/efficientFilter")
public class MachineEfficientFilterController {

    @Autowired
    private MachineEfficientFilterService machineEfficientFilterService;

    @GetMapping("/replaceStatus")
    public ResultData getReplaceStatus(@RequestParam String qrcode) {
        ResultData res = new ResultData();
        Map<String, Object> resData = new HashMap<>();

        ResultData response = machineEfficientFilterService.fetchByQRCode(qrcode);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            res.setResponseCode(ResponseCode.RESPONSE_ERROR);
            res.setDescription("fetch machineEfficientFilter failed");
            return res;
        }

        MachineEfficientFilter selectedOne = (MachineEfficientFilter) response.getData();
        resData.put("qrcode", qrcode);
        resData.put("replaceStatus", selectedOne.getReplaceStatus());
        res.setData(resData);
        return res;
    }

    @GetMapping("/replaceRemind/isOpen")
    public ResultData replaceRemindIsOpen(@RequestParam String qrcode) {
        ResultData res = new ResultData();
        Map<String, Object> resData = new HashMap<>();
        resData.put("qrcode", qrcode);

        ResultData response = machineEfficientFilterService.fetchByQRCode(qrcode);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            res.setResponseCode(ResponseCode.RESPONSE_ERROR);
            res.setDescription("fetch machineEfficientFilter failed");
            return res;
        }

        MachineEfficientFilter selectedOne = (MachineEfficientFilter) response.getData();
        resData.put("isOpen", selectedOne.isReplaceRemindOn());
        res.setData(resData);
        return res;
    }

    @PostMapping("/replaceRemind/status/change")
    public ResultData changeReplaceRemindStatus(@RequestParam String qrcode,
                                                @RequestParam boolean replaceRemindStatus) {
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
        condition.put("replaceRemindOn", replaceRemindStatus);
        condition.put("qrcode", qrcode);
        ResultData response = machineEfficientFilterService.modify(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            res.setResponseCode(ResponseCode.RESPONSE_ERROR);
            res.setDescription("modify machineEfficientFilter failed");
            return res;
        }

        res.setData(resData);
        return res;
    }

    @GetMapping("/replace/confirm")
    public ResultData confirmReplace(@RequestParam String qrcode) {
        ResultData res = new ResultData();
        Map<String, Object> resData = new HashMap<>();

        //检测参数
        if (StringUtils.isEmpty(qrcode)) {
            res.setResponseCode(ResponseCode.RESPONSE_ERROR);
            res.setDescription("qrcode cannot be empty");
            return res;
        }

        //将replaceStatus字段修改为0，将isRemindedStatus字段修改为0
        //TODO 调用core服务中的方法重置该设备高效滤网的使用时间计时
        Map<String, Object> condition = new HashMap<>();
        condition.put("qrcode", qrcode);
        condition.put("replaceStatus", EfficientFilterStatus.NO_NEED);
        condition.put("isRemindedStatus", EfficientFilterRemindStatus.REMIND_ZERO);
        ResultData response = machineEfficientFilterService.modify(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            res.setResponseCode(ResponseCode.RESPONSE_ERROR);
            res.setDescription("modify machineFilterClean failed");
            return res;
        }

        resData.put("qrcode", qrcode);
        res.setData(resData);
        return res;
    }
}
