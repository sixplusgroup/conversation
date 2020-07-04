package finley.gmair.controller;

import finley.gmair.model.machine.MachineFilterClean;
import finley.gmair.service.MachineFilterCleanService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: Bright Chan
 * @date: 2020/7/3 10:54
 * @description: TODO
 */

@RestController
@RequestMapping("/machine/filter/clean")
public class MachineFilterCleanController {

    private static final int CLEAN_TIME_INTERVAL = 30;
    private static final String TRUE = "true";
    private static final String FALSE = "false";

    @Autowired
    private MachineFilterCleanService machineFilterCleanService;

    /**
     * 查询设备初效滤网是否需要清洗
     * 判断逻辑：查询MachineFilterClean表中对应字段，若该字段中的isNeedClean属性为true，
     * 则直接判断为需要清洗；若为false，则将lastConfirmTime与请求时间做对比，若相差大于等于30天，
     * 则判断为需要清洗，否则判断为不需要清洗。
     * @return ResultData，若返回成功，则data字段中包含qrcode和isNeedClean两个属性。
     */
    @GetMapping("/")
    public ResultData filterNeedCleanOrNot(@RequestParam String qrcode) {
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
        condition.put("blockFlag", false);
        ResultData response = machineFilterCleanService.fetch(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            res.setResponseCode(ResponseCode.RESPONSE_ERROR);
            res.setDescription("fetch machineFilterClean failed");
            return res;
        }

        MachineFilterClean selectedOne = (MachineFilterClean) response.getData();
        if (selectedOne.isNeedClean()) {
            resData.put("isNeedClean", selectedOne.isNeedClean());
        }
        else {
            long dayDiff = (new Date().getTime() - selectedOne.getLastConfirmTime().getTime()) /
                            (1000 * 60 * 60 * 24);
            if (dayDiff >= CLEAN_TIME_INTERVAL) {
                resData.put("isNeedClean", true);
                //更新isNeedClean字段
                Map<String, Object> modification = new HashMap<>();
                modification.put("qrcode", qrcode);
                modification.put("isNeedClean", true);
                ResultData modifyRes = machineFilterCleanService.modify(modification);
                if (modifyRes.getResponseCode() != ResponseCode.RESPONSE_OK) {
                    res.setResponseCode(ResponseCode.RESPONSE_ERROR);
                    res.setDescription("modify MachineFilterClean failed");
                    return res;
                }
            }
            else {
                resData.put("isNeedClean", false);
            }
        }
        res.setData(resData);
        return res;
    }

    /**
     * 查询设备初效滤网清洗提醒是否开启
     * @return ResultData，若返回成功，则data字段中包含qrcode和isOpen两个属性。
     */
    @GetMapping("/isOpen")
    public ResultData filterCleanRemindIsOpen(@RequestParam String qrcode) {
        ResultData res = new ResultData();
        Map<String, Object> resData = new HashMap<>();

        //检测参数
        if (StringUtils.isEmpty(qrcode)) {
            res.setResponseCode(ResponseCode.RESPONSE_ERROR);
            res.setDescription("qrcode cannot be empty");
            return res;
        }

        Map<String, Object> condition = new HashMap<>();
        condition.put("qrcode", qrcode);
        condition.put("blockFlag", false);
        ResultData response = machineFilterCleanService.fetch(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            res.setResponseCode(ResponseCode.RESPONSE_ERROR);
            res.setDescription("fetch machineFilterClean failed");
            return res;
        }

        MachineFilterClean selectedOne = (MachineFilterClean) response.getData();
        resData.put("qrcode", qrcode);
        resData.put("isOpen", selectedOne.isCleanRemindStatus());
        res.setData(resData);
        return res;
    }

    /**
     * 改变设备初效滤网清洗提醒开启状态
     * @return ResultData，若返回成功，则data字段中包含qrcode属性。
     */
    @PostMapping("/change")
    public ResultData changeFilterCleanRemindStatus(@RequestParam String qrcode,
                                                    @RequestParam String cleanRemindStatus) {
        ResultData res = new ResultData();
        Map<String, Object> resData = new HashMap<>();
        resData.put("qrcode", qrcode);

        //检测参数
        if (StringUtils.isEmpty(qrcode) || StringUtils.isEmpty(cleanRemindStatus)) {
            res.setResponseCode(ResponseCode.RESPONSE_ERROR);
            res.setDescription("qrcode or cleanRemindStatus cannot be empty");
            return res;
        }

        Map<String, Object> condition = new HashMap<>();
        condition.put("qrcode", qrcode);
        if (TRUE.equals(cleanRemindStatus)) {
            condition.put("cleanRemindStatus", true);
        }
        else if (FALSE.equals(cleanRemindStatus)) {
            condition.put("cleanRemindStatus", false);
        }
        else {
            res.setResponseCode(ResponseCode.RESPONSE_ERROR);
            res.setDescription("cleanRemindStatus is illegal");
            return res;
        }
        ResultData response = machineFilterCleanService.modify(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            res.setResponseCode(ResponseCode.RESPONSE_ERROR);
            res.setDescription("modify machineFilterClean failed");
            return res;
        }

        res.setData(resData);
        return res;
    }

    /**
     * 确认清洗
     * @return ResultData，若返回成功，则data字段中包含qrcode和createAt两个属性。
     */
    @GetMapping("/confirm")
    public ResultData confirmClean(@RequestParam String qrcode) {
        ResultData res = new ResultData();
        Map<String, Object> resData = new HashMap<>();

        //检测参数
        if (StringUtils.isEmpty(qrcode)) {
            res.setResponseCode(ResponseCode.RESPONSE_ERROR);
            res.setDescription("qrcode cannot be empty");
            return res;
        }

        //将isNeedClean字段修改为false，将lastConfirmTime字段修改为当前时间
        Map<String, Object> condition = new HashMap<>();
        Date now = new Date();
        condition.put("qrcode", qrcode);
        condition.put("isNeedClean", false);
        condition.put("lastConfirmTime", now);
        ResultData response = machineFilterCleanService.modify(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            res.setResponseCode(ResponseCode.RESPONSE_ERROR);
            res.setDescription("modify machineFilterClean failed");
            return res;
        }

        resData.put("qrcode", qrcode);
        resData.put("createAt", now.getTime());
        res.setData(resData);
        return res;
    }
}
