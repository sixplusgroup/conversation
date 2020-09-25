package finley.gmair.controller;

import finley.gmair.model.machine.*;
import finley.gmair.pool.MachinePool;
import finley.gmair.service.*;
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

    @Autowired
    private MachineEfficientFilterService machineEfficientFilterService;

    @Autowired
    private CoreV3Service coreV3Service;

    @Autowired
    private MachineQrcodeBindService machineQrcodeBindService;

    @Autowired
    private QRCodeService qrCodeService;

    @Autowired
    private ModelEfficientConfigService modelEfficientConfigService;

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

    @PostMapping("/update/specifiedMachine/filterStatus/hourly")
    public ResultData specifiedMachineFilterStatusHourlyUpdate() {
        //avoid exception: read timed out at Timing service side.
        MachinePool.getMachinePool().execute(() -> {
            ResultData res = machineEfficientFilterService.specifiedMachineFilterStatusHourlyUpdate();
            if (res.getResponseCode() != ResponseCode.RESPONSE_OK) {
                logger.error("hourly update: specified machine efficient filter status. failed!");
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
            return res;
        }
        else {
            //调用core服务中的方法重置该设备高效滤网的使用时间计时
            MachinePool.getMachinePool().execute(() -> {
                condition.clear();
                condition.put("codeValue", qrcode);
                condition.put("blockFlag", false);
                ResultData machineQRCodeRes = machineQrcodeBindService.fetch(condition);
                //得到uid
                if (machineQRCodeRes.getResponseCode() == ResponseCode.RESPONSE_OK) {
                    List<MachineQrcodeBindVo> tmpStore = (List<MachineQrcodeBindVo>) machineQRCodeRes.getData();
                    String machineId = tmpStore.get(0).getMachineId();
                    condition.clear();
                    condition.put("codeValue", qrcode);
                    condition.put("blockFlag", false);
                    machineQRCodeRes = qrCodeService.fetch(condition);
                    //得到qrcode
                    if (machineQRCodeRes.getResponseCode() == ResponseCode.RESPONSE_OK){
                        String modelId = ((List<QRCode>)machineQRCodeRes.getData()).get(0).getModelId();
                        condition.clear();
                        condition.put("modelId", modelId);
                        condition.put("blockFlag", false);
                        ResultData modelEfficientRes = modelEfficientConfigService.fetch(condition);
                        //得到重置时间
                        if (modelEfficientRes.getResponseCode() == ResponseCode.RESPONSE_OK){
                            int resetHour = ((List<ModelEfficientConfig>)modelEfficientRes.getData()).get(0).getResetHour();
                            //发消息
                            coreV3Service.resetSurplus(machineId, resetHour);
                        }
                    }

                }
            });
            resData.put("qrcode", qrcode);
            res.setData(resData);
        }
        return res;
    }

    /**
     * 由mqtt-core服务调用，用于更新replace_status字段
     * @param remain mqtt-core传来的设备剩余可用时间
     * @param uid 设备uid
     * @return 执行结果
     */
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
