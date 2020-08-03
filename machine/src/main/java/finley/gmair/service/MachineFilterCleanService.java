package finley.gmair.service;

import finley.gmair.util.ResultData;

import java.util.Map;

/**
 * @author: Bright Chan
 * @date: 2020/7/4 13:31
 * @description: TODO
 */
public interface MachineFilterCleanService {

    ResultData fetch(Map<String, Object> condition);

    ResultData fetchOne(Map<String, Object> condition);

    ResultData fetchNeedRemind();

    /**
     * 返回值中的data字段只包含一个对象
     * @param qrcode 设备二维码
     * @return 查询结果
     */
    ResultData fetchByQRCode(String qrcode);

    ResultData modify(Map<String, Object> condition);

    ResultData updateIsNeedClean(Map<String, Object> condition);

    /**
     * 判断被选中的设备是否需要清洗
     * 判断逻辑：查询MachineFilterClean表中对应字段，若该字段中的isNeedClean属性为true，
     * 则直接判断为需要清洗；若为false，则将lastConfirmTime与请求时间做对比，若相差大于等于30天，
     * 则判断为需要清洗，否则判断为不需要清洗。
     * @param qrcode 被选中的设备的二维码
     * @return 判断结果
     */
    ResultData filterCleanCheck(String qrcode);

    /**
     * 每天中午十二点查看并更新machine_filter_clean表
     * 向需要提醒进行滤网清洗的用户微信推送提醒
     * @return 执行结果
     */
    ResultData filterCleanDailyCheck();

    /**
     * 新增绑定用户的时候调用此方法新增machine_filter_clean表数据
     * @param qrcode 新增绑定的二维码
     * @return 新增结果
     */
    ResultData addNewBindMachine(String qrcode);

    /**
     * 发送微信公众号清洗提醒
     * @param qrcode 设备二维码
     * @return 发送结果
     */
    ResultData sendWeChatMessage(String qrcode);

    /**
     * 检查给定的qrcode对应的设备是否是具有初效滤网的设备
     * @param qrcode 设备二维码
     * @return 检查结果
     */
    boolean isCorrectGoods(String qrcode);
}
