package finley.gmair.controller;

import finley.gmair.service.MaintenanceService;
import finley.gmair.util.ResultData;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 设备检修交互接口
 *
 * @author lycheeshell
 * @date 2021/1/21 20:02
 */
@CrossOrigin
@RestController
@RequestMapping("/management/maintenance")
public class MaintenanceController {

    @Autowired
    private MaintenanceService maintenanceService;

    /**
     * 查询设备最新的状态信息
     *
     * @param qrcode 二维码
     * @return 设备最新的状态信息
     */
    @GetMapping(value = "/getLatestStatus")
    public ResultData getLatestStatus(String qrcode) {
        return maintenanceService.getLatestStatus(qrcode);
    }

    /**
     * 查询设备的定时配置
     *
     * @param qrcode 二维码
     * @return 设备定时数据
     */
    @GetMapping(value = "/getTimingSetting")
    public ResultData getTimingSetting(String qrcode) {
        return maintenanceService.getTimingSetting(qrcode);
    }

    /**
     * 查询v3版本的设备现存告警信息
     *
     * @param qrcode 二维码
     * @param code   告警码
     * @return 设备告警列表
     */
    @GetMapping(value = "/getAlertList")
    public ResultData getAlertList(String qrcode, Integer code) {
        return maintenanceService.getAlertList(qrcode, code);
    }

    /**
     * 消除v3设备的警报
     *
     * @param machineId 设备mac
     * @param code      告警码
     * @return 消除操作的结果
     */
    @PostMapping(value = "/removeAlert")
    public ResultData removeAlert(String machineId, Integer code) {
        return maintenanceService.removeAlert(machineId, code);
    }

    /**
     * 查询用户设备的操作历史
     *
     * @param consumerId 用户电话
     * @param qrcode     设备二维码
     * @param pageIndex    第几页
     * @param pageSize     页大小
     * @return 用户设备操作历史
     */
    @GetMapping(value = "/getOperationHistory")
    public ResultData getOperationHistory(String consumerId, String qrcode, Integer pageIndex, Integer pageSize) {
        return maintenanceService.getOperationHistory(consumerId, qrcode, pageIndex, pageSize);
    }

    /**
     * 刷新设备状态
     *
     * @param qrcode 二维码
     * @return 刷新的结果
     */
    @PostMapping(value = "/refreshStatus")
    public ResultData refreshStatus(String qrcode) {
        return maintenanceService.refreshStatus(qrcode);
    }

    /**
     * 配置设备的定时
     *
     * @param qrcode      设备二维码
     * @param startHour   开始时间，小时
     * @param startMinute 开始时间，分钟
     * @param endHour     结束时间，小时
     * @param endMinute   结束时间，分钟
     * @param status      开启和关闭的状态
     * @return 配置结果
     */
    @PostMapping(value = "/setTiming")
    public ResultData setTiming(String qrcode, int startHour, int startMinute, int endHour, int endMinute, boolean status) {
        return maintenanceService.setTiming(qrcode, startHour, startMinute, endHour, endMinute, status);
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

        return maintenanceService.isCleanNeed(qrcode);
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

        return maintenanceService.isCleanRemindOpen(qrcode);
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
        return maintenanceService.setCleanRemindStatus(qrcode, cleanRemindStatus);
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

        return maintenanceService.isReplaceNeed(qrcode);
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

        return maintenanceService.isReplaceRemindOpen(qrcode);
    }

    /**
     * 设置设备高效滤网清洗提醒开启状态
     *
     * @param qrcode              二维码
     * @param replaceRemindStatus 是否开启提醒
     * @return 操作结果
     */
    @PostMapping(value = "/setReplaceRemindStatus")
    public ResultData setReplaceRemindStatus(String qrcode, Boolean replaceRemindStatus) {
        return maintenanceService.setReplaceRemindStatus(qrcode, replaceRemindStatus);
    }

    /**
     * 创建设备上门检修
     *
     * @param consumerConsignee 客户姓名
     * @param consumerPhone     客户电话
     * @param consumerAddress   客户地址
     * @param model             型号
     * @param description       备注
     * @return 创建结果
     */
    @PostMapping(value = "/createInspectTask")
    public ResultData createInspectTask(String consumerConsignee, String consumerPhone, String consumerAddress,
                                        String model, String description) {
        return maintenanceService.createInspectTask(consumerConsignee, consumerPhone, consumerAddress, model, description);
    }

    /**
     * 根据二维码查看设备型号
     *
     * @param qrcode 二维码
     * @return 设备型号
     */
    @GetMapping(value = "/getModelByQrcode")
    public ResultData getModelByQrcode(String qrcode) {
        return maintenanceService.getModelByQrcode(qrcode);
    }

}