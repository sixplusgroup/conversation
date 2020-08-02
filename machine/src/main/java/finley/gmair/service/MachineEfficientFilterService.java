package finley.gmair.service;

import finley.gmair.model.machine.EfficientFilterStatus;
import finley.gmair.util.ResultData;

import java.util.Map;

/**
 * @author: Bright Chan
 * @date: 2020/7/26 11:24
 * @description: TODO
 */
public interface MachineEfficientFilterService {

    ResultData fetch(Map<String, Object> condition);

    /**
     * 返回值的data中只包含一个符合条件的对象
     * @param qrcode 设备二维码
     * @return 查询结果
     */
    ResultData fetchByQRCode(String qrcode);

    ResultData modify(Map<String, Object> condition);

    /**
     * 新增绑定用户的时候调用此方法新增machine_efficient_filter表数据
     * @param qrcode 新增绑定的二维码
     * @return 新增结果
     */
    ResultData addNewBindMachine(String qrcode);

    /**
     * 得到需要第一次提醒的设备列表
     * @return 设备列表
     */
    ResultData fetchNeedRemindFirst();

    /**
     * 得到需要第二次提醒的设备列表
     * @return 设备列表
     */
    ResultData fetchNeedRemindSecond();

    /**
     * 发送微信公众号清洗提醒
     * @param qrcode 设备二维码, number提醒次数（1/2）
     * @return 发送结果
     */
    ResultData sendWeChatMessage(String qrcode, int number);

    /**
     * 每小时查看machine_efficient_filter表
     * 向需要提醒进行高效滤网更换的用户微信推送提醒
     * @return 执行结果
     */
    ResultData efficientFilterHourlyCheck();

    /**
     * 通过当前滤芯剩余寿命判断其处于什么状态
     * @param remain 滤芯剩余寿命
     * @return 滤芯状态
     */
    EfficientFilterStatus checkEfficientFilterStatus(int remain);
}
