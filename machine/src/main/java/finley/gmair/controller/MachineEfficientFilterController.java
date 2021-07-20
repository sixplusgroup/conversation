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

import javax.annotation.Resource;
import java.util.Date;
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

    @Resource
    private BoardVersionService boardVersionService;

    @Autowired
    private CoreV3Service coreV3Service;

    @Resource
    private CoreV2Service coreV2Service;

    @Autowired
    private MachineQrcodeBindService machineQrcodeBindService;

    @Autowired
    private QRCodeService qrCodeService;

    @Autowired
    private ModelEfficientConfigService modelEfficientConfigService;

    @Autowired
    private MachineEfficientInfoService machineEfficientInfoService;

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

    @PostMapping("/update/specifiedMachine/filterStatus/daily")
    public ResultData specifiedMachineFilterStatusDailyUpdate() {
        //avoid exception: read timed out at Timing service side.
        MachinePool.getMachinePool().execute(() -> {
            ResultData res = machineEfficientFilterService.specifiedMachineFilterStatusDailyUpdate();
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
        ResultData result = new ResultData();
        Map<String, Object> data = new HashMap<>();

        //检测参数
        if (StringUtils.isEmpty(qrcode)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("qrcode cannot be empty");
            return result;
        }

        //将replaceStatus字段修改为0，将isRemindedStatus字段修改为0
        Map<String, Object> condition = new HashMap<>();
        condition.put("qrcode", qrcode);
        condition.put("replaceStatus", EfficientFilterStatus.NO_NEED.getValue());
        condition.put("isRemindedStatus", EfficientFilterRemindStatus.REMIND_ZERO.getValue());
        ResultData response = machineEfficientFilterService.modify(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("modify machineFilterClean failed");
            return result;
        }

        //调用core服务中的方法重置该设备高效滤网的使用时间计时
        MachinePool.getMachinePool().execute(() -> {
            Map<String, Object> con = new HashMap<>();
            con.put("codeValue", qrcode);
            con.put("blockFlag", false);
            ResultData temp = machineQrcodeBindService.fetch(con);

            if (temp.getResponseCode() != ResponseCode.RESPONSE_OK) {
                logger.error("[Error] fail to find qrcode bind record for: " + qrcode);
                return;
            }

            //得到uid
            List<MachineQrcodeBindVo> tmpStore = (List<MachineQrcodeBindVo>) temp.getData();
            String machineId = tmpStore.get(0).getMachineId();

            //获取machineId对应的版本
            con.clear();
            con.put("machineId", machineId);
            con.put("blockFlag", false);
            temp = boardVersionService.fetchBoardVersion(con);
            if (temp.getResponseCode() != ResponseCode.RESPONSE_OK) {
                logger.error("[Error] fail to find board version record for : " + qrcode);
                return;
            }
            BoardVersion version = ((List<BoardVersion>) temp.getData()).get(0);
            //威霖设备处理方法
            if (version.getVersion() == 3) {
                con.clear();
                con.put("codeValue", qrcode);
                con.put("blockFlag", false);
                temp = qrCodeService.fetch(con);

                //得到resetHour
                if (temp.getResponseCode() == ResponseCode.RESPONSE_OK) {
                    String modelId = ((List<QRCode>) temp.getData()).get(0).getModelId();
                    con.clear();
                    con.put("modelId", modelId);
                    con.put("blockFlag", false);
                    ResultData modelEfficientRes = modelEfficientConfigService.fetch(con);
                    //得到重置时间
                    if (modelEfficientRes.getResponseCode() == ResponseCode.RESPONSE_OK) {
                        int resetHour = ((List<ModelEfficientConfig>) modelEfficientRes.getData()).get(0).getResetHour();
                        //发消息
                        coreV3Service.resetSurplus(machineId, resetHour);
                    }
                }
            }
            //锐竹设备处理方法
            if (version.getVersion() == 2) {
                con.clear();
                con.put("qrcode", qrcode);
                con.put("lastConfirmTime", new Date());
                machineEfficientInfoService.modify(con);
                coreV2Service.configScreen(machineId, 0);
                logger.info("request to config sreeen for machine: " + machineId + " with value 0");
            }

            //version=1
            if (version.getVersion() == 1) {
                con.clear();
                con.put("qrcode", qrcode);
                con.put("lastConfirmTime", new Date());
                machineEfficientInfoService.modify(con);
            }
        });
        data.put("qrcode", qrcode);
        result.setData(data);
        return result;
    }

    /**
     * 由mqtt-core服务调用，用于更新replace_status字段
     *
     * @param remain mqtt-core传来的设备剩余可用时间
     * @param uid    设备uid
     * @return 执行结果
     */
    @GetMapping("/updateByRemain")
    public ResultData updateByRemain(@RequestParam int remain, @RequestParam String uid) {
        ResultData res = new ResultData();
        //检测参数
        if (StringUtils.isEmpty(uid)) {
            res.setResponseCode(ResponseCode.RESPONSE_ERROR);
            res.setDescription("qrcode cannot be empty");
            return res;
        }
        ResultData response = machineEfficientFilterService.updateByRemain(remain, uid);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            res.setResponseCode(ResponseCode.RESPONSE_ERROR);
            res.setDescription("updateByRemain failed");
            return res;
        }
        return res;
    }
}
