package finley.gmair.controller;

import finley.gmair.model.machine.EfficientFilterRemindStatus;
import finley.gmair.model.machine.EfficientFilterStatus;
import finley.gmair.model.machine.MachineEfficientFilter;
import finley.gmair.pool.MachinePool;
import finley.gmair.service.CoreV3Service;
import finley.gmair.service.MachineEfficientFilterService;
import finley.gmair.service.MachineQrcodeBindService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.vo.machine.MachineQrcodeBindVo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Bright Chan
 * @date: 2020/7/24 16:34
 * @description: 设备高效滤网控制类
 */

@RestController
@RequestMapping("/machine/efficientFilter")
public class MachineEfficientFilterController {

    private Logger logger = LoggerFactory.getLogger(MachineEfficientFilterController.class);

    private static final int REMAIN = 2160;

    @Autowired
    private MachineEfficientFilterService machineEfficientFilterService;

    @Autowired
    private CoreV3Service coreV3Service;

    @Autowired
    private MachineQrcodeBindService machineQrcodeBindService;

    @PostMapping("/check/hourly")
    public ResultData efficientFilterHourlyCheck() {
        //avoid exception: read timed out at Timing service side.
        MachinePool.getMachinePool().execute(() -> {
            ResultData res = machineEfficientFilterService.efficientFilterHourlyCheck();
            if (res.getResponseCode() != ResponseCode.RESPONSE_OK) {
                logger.error("hourly check: efficient filter. failed!");
            }
        });
        return new ResultData();
    }

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
        Map<String, Object> condition = new HashMap<>();
        condition.put("qrcode", qrcode);
        condition.put("replaceStatus", EfficientFilterStatus.NO_NEED.getValue());
        condition.put("isRemindedStatus", EfficientFilterRemindStatus.REMIND_ZERO.getValue());
        ResultData response = machineEfficientFilterService.modify(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            res.setResponseCode(ResponseCode.RESPONSE_ERROR);
            res.setDescription("modify machineFilterClean failed");
        }
        else {
            //调用core服务中的方法重置该设备高效滤网的使用时间计时
            MachinePool.getMachinePool().execute(() -> {
                condition.clear();
                condition.put("codeValue", qrcode);
                condition.put("blockFlag", false);
                ResultData machineQRCodeRes = machineQrcodeBindService.fetch(condition);
                if (machineQRCodeRes.getResponseCode() == ResponseCode.RESPONSE_OK) {
                    List<MachineQrcodeBindVo> tmpStore = (List<MachineQrcodeBindVo>) machineQRCodeRes.getData();
                    String machineId = tmpStore.get(0).getMachineId();
                    coreV3Service.resetSurplus(machineId, REMAIN);
                }
            });
            resData.put("qrcode", qrcode);
            res.setData(resData);
        }
        return res;
    }

    @GetMapping("/updateByRemain")
    public ResultData updateByRemain(@RequestParam int remain,@RequestParam String uid) {
        ResultData res = new ResultData();
        //检测参数
        if (StringUtils.isEmpty(uid)) {
            res.setResponseCode(ResponseCode.RESPONSE_ERROR);
            res.setDescription("qrcode cannot be empty");
            return res;
        }
        ResultData response = machineEfficientFilterService.updateByRemain(remain,uid);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            res.setResponseCode(ResponseCode.RESPONSE_ERROR);
            res.setDescription("updateByRemain failed");
            return res;
        }
        return res;
    }
}
