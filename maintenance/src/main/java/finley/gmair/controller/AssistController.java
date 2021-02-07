package finley.gmair.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import finley.gmair.model.machine.BoardVersion;
import finley.gmair.service.MachineService;
import finley.gmair.service.MqttService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.vo.machine.MachineQrcodeBindVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户辅助操作的交互接口
 *
 * @author lycheeshell
 * @date 2021/1/19 16:13
 */
@RestController
@RequestMapping("/maintenance/assist")
public class AssistController {

    @Resource
    private MqttService mqttService;

    @Resource
    private MachineService machineService;

    /**
     * 刷新设备状态
     *
     * @param qrcode 二维码
     * @return 刷新的结果
     */
    @PostMapping(value = "/refreshStatus")
    public ResultData refreshStatus(String qrcode) {
        if (StringUtils.isEmpty(qrcode)) {
            return ResultData.error("qrcode为空");
        }

        // 检查二维码是不是v3版本
        ResultData boardVersionResponse= machineService.findBoardVersionByQRcode(qrcode);
        if (boardVersionResponse.getResponseCode() != ResponseCode.RESPONSE_OK) {
            return ResultData.error("根据二维码查找设备版本失败");
        }
        List<BoardVersion> list = (List<BoardVersion>) boardVersionResponse.getData();
        if (list == null || list.size() == 0) {
            return ResultData.error("根据二维码查找设备版本失败");
        }
        if (list.get(0).getVersion() < 3) {
            return ResultData.empty("设备版本过低，暂时不支持刷新状态");
        }

        // 1.根据二维码查询机器MAC
        ResultData response = machineService.findMachineIdByCodeValueFacetoConsumer(qrcode);
        if(response.getResponseCode() != ResponseCode.RESPONSE_OK){
            return ResultData.error("根据二维码查找机器MAC失败");
        }
        MachineQrcodeBindVo machineQrcodeBindVo = JSONArray.parseArray(JSONObject.toJSONString(response.getData()),MachineQrcodeBindVo.class).get(0);
        String machineId = machineQrcodeBindVo.getMachineId();

        // 2.刷新设备状态
        return mqttService.demandReport(machineId);
    }

    /**
     * 配置设备的定时
     *
     * @param qrcode 设备二维码
     * @param startHour 开始时间，小时
     * @param startMinute 开始时间，分钟
     * @param endHour 结束时间，小时
     * @param endMinute 结束时间，分钟
     * @param status 开启和关闭的状态
     * @return 配置结果
     */
    @PostMapping(value = "/setTiming")
    public ResultData setTiming(String qrcode, int startHour, int startMinute, int endHour, int endMinute, boolean status) {
        return machineService.configConfirm(qrcode, startHour, startMinute, endHour, endMinute, status);
    }

    /**
     * 检查初效滤网是否需要清洗
     *
     * @param qrcode 二维码
     * @return 初效滤网是否需要清洗
     */
    @GetMapping(value = "/isCleanNeed")
    public ResultData isCleanNeed(String qrcode) {
        if (StringUtils.isEmpty(qrcode)) {
            return ResultData.error("qrcode为空");
        }

        return machineService.filterNeedCleanOrNot(qrcode);
    }

    /**
     * 初效滤网清洗提醒是否开启
     *
     * @param qrcode 二维码
     * @return 提醒是否开启
     */
    @GetMapping(value = "/isCleanRemindOpen")
    public ResultData isCleanRemindOpen(String qrcode) {
        if (StringUtils.isEmpty(qrcode)) {
            return ResultData.error("qrcode为空");
        }

        return machineService.filterCleanRemindIsOpen(qrcode);
    }

    /**
     * 设置设备初效滤网清洗提醒开启状态
     *
     * @param qrcode            二维码
     * @param cleanRemindStatus 是否开启提醒
     * @return 操作结果
     */
    @PostMapping(value = "/setCleanRemindStatus")
    public ResultData setCleanRemindStatus(String qrcode, Boolean cleanRemindStatus) {
        if (StringUtils.isEmpty(qrcode)) {
            return ResultData.error("qrcode为空");
        }
        if (cleanRemindStatus == null) {
            return ResultData.error("提醒状态为空");
        }

        return machineService.changeFilterCleanRemindStatus(qrcode, cleanRemindStatus);
    }

    /**
     * 高效滤网是否需要更换
     *
     * @param qrcode 二维码
     * @return 是否需要更换
     */
    @GetMapping(value = "/isReplaceNeed")
    public ResultData isReplaceNeed(String qrcode) {
        if (StringUtils.isEmpty(qrcode)) {
            return ResultData.error("qrcode为空");
        }

        return machineService.getReplaceStatus(qrcode);
    }

    /**
     * 高效滤网更换提醒是否开启
     *
     * @param qrcode 二维码
     * @return 提醒是否开启
     */
    @GetMapping(value = "/isReplaceRemindOpen")
    public ResultData isReplaceRemindOpen(String qrcode) {
        if (StringUtils.isEmpty(qrcode)) {
            return ResultData.error("qrcode为空");
        }

        return machineService.replaceRemindIsOpen(qrcode);
    }

    /**
     * 设置设备高效滤网清洗提醒开启状态
     *
     * @param qrcode 二维码
     * @param replaceRemindStatus 是否开启提醒
     * @return 操作结果
     */
    @PostMapping(value = "/setReplaceRemindStatus")
    public ResultData setReplaceRemindStatus(String qrcode, Boolean replaceRemindStatus) {
        if (StringUtils.isEmpty(qrcode)) {
            return ResultData.error("qrcode为空");
        }
        if (replaceRemindStatus == null) {
            return ResultData.error("提醒状态为空");
        }

        return machineService.changeReplaceRemindStatus(qrcode, replaceRemindStatus);
    }

}
