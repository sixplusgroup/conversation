package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 调用maintenance服务
 *
 * @author lycheeshell
 * @date 2021/1/21 20:05
 */
@FeignClient("maintenance-agent")
public interface MaintenanceService {

    /**
     * 查询设备最新的状态信息
     *
     * @param qrcode 二维码
     * @return 设备最新的状态信息
     */
    @GetMapping("/maintenance/machine/getLatestStatus")
    ResultData getLatestStatus(@RequestParam("qrcode") String qrcode);

    /**
     * 查询设备的定时配置
     *
     * @param qrcode 二维码
     * @return 设备定时数据
     */
    @GetMapping("/maintenance/machine/getTimingSetting")
    ResultData getTimingSetting(@RequestParam("qrcode") String qrcode);

    /**
     * 查询v3版本的设备现存告警信息
     *
     * @param qrcode 二维码
     * @param code   告警码
     * @return 设备告警列表
     */
    @GetMapping("/maintenance/alert/getAlertList")
    ResultData getAlertList(@RequestParam("qrcode") String qrcode, @RequestParam("code") Integer code);

    /**
     * 消除v3设备的警报
     *
     * @param machineId 设备mac
     * @param code      告警码
     * @return 消除操作的结果
     */
    @PostMapping("/maintenance/alert/removeAlert")
    ResultData removeAlert(@RequestParam("machineId") String machineId, @RequestParam("code") Integer code);

    /**
     * 查询用户设备的操作历史
     *
     * @param consumerId 用户电话
     * @param qrcode     设备二维码
     * @param pageIndex    第几页
     * @param pageSize     页大小
     * @return 用户设备操作历史
     */
    @GetMapping("/maintenance/history/getOperationHistory")
    ResultData getOperationHistory(@RequestParam("consumerId") String consumerId, @RequestParam("qrcode") String qrcode,
                                   @RequestParam("pageIndex") Integer pageIndex, @RequestParam("pageSize") Integer pageSize);

    /**
     * 刷新设备状态
     *
     * @param qrcode 二维码
     * @return 刷新的结果
     */
    @PostMapping("/maintenance/assist/refreshStatus")
    ResultData refreshStatus(@RequestParam("qrcode") String qrcode);

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
    @PostMapping("/maintenance/assist/setTiming")
    ResultData setTiming(@RequestParam("qrcode") String qrcode, @RequestParam("startHour") int startHour,
                         @RequestParam("startMinute") int startMinute, @RequestParam("endHour") int endHour,
                         @RequestParam("endMinute") int endMinute, @RequestParam("status") boolean status);

    /**
     * 检查初效滤网是否需要清洗
     *
     * @param qrcode 二维码
     * @return 初效滤网是否需要清洗
     */
    @GetMapping("/maintenance/assist/isCleanNeed")
    ResultData isCleanNeed(@RequestParam("qrcode") String qrcode);

    /**
     * 初效滤网清洗提醒是否开启
     *
     * @param qrcode 二维码
     * @return 提醒是否开启
     */
    @GetMapping("/maintenance/assist/isCleanRemindOpen")
    ResultData isCleanRemindOpen(@RequestParam("qrcode") String qrcode);

    /**
     * 设置设备初效滤网清洗提醒开启状态
     *
     * @param qrcode            二维码
     * @param cleanRemindStatus 是否开启提醒
     * @return 操作结果
     */
    @PostMapping("/maintenance/assist/setCleanRemindStatus")
    ResultData setCleanRemindStatus(@RequestParam("qrcode") String qrcode, @RequestParam("cleanRemindStatus") Boolean cleanRemindStatus);

    /**
     * 高效滤网是否需要更换
     *
     * @param qrcode 二维码
     * @return 是否需要更换
     */
    @GetMapping("/maintenance/assist/isReplaceNeed")
    ResultData isReplaceNeed(@RequestParam("qrcode") String qrcode);

    /**
     * 高效滤网更换提醒是否开启
     *
     * @param qrcode 二维码
     * @return 提醒是否开启
     */
    @GetMapping("/maintenance/assist/isReplaceRemindOpen")
    ResultData isReplaceRemindOpen(@RequestParam("qrcode") String qrcode);

    /**
     * 设置设备高效滤网清洗提醒开启状态
     *
     * @param qrcode              二维码
     * @param replaceRemindStatus 是否开启提醒
     * @return 操作结果
     */
    @PostMapping("/maintenance/assist/setReplaceRemindStatus")
    ResultData setReplaceRemindStatus(@RequestParam("qrcode") String qrcode, @RequestParam("replaceRemindStatus") Boolean replaceRemindStatus);

    /**
     * 调度人员创建安装任务，带备注
     *
     * @param consumerConsignee 客户姓名
     * @param consumerPhone     客户电话
     * @param consumerAddress   客户地址
     * @param model             设备型号
     * @param description       备注
     * @return 创建结果
     */
    @PostMapping("/maintenance/inspect/createInspectTask")
    ResultData createInspectTask(@RequestParam("consumerConsignee") String consumerConsignee,
                                 @RequestParam("consumerPhone") String consumerPhone,
                                 @RequestParam("consumerAddress") String consumerAddress,
                                 @RequestParam(value = "model") String model,
                                 @RequestParam(value = "description", required = false) String description);

    /**
     * 根据二维码查看设备型号
     *
     * @param qrcode 二维码
     * @return 设备型号
     */
    @GetMapping("/maintenance/assist/getModelByQrcode")
    ResultData getModelByQrcode(@RequestParam("qrcode")String qrcode);
}
