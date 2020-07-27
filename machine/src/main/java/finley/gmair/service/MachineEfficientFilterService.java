package finley.gmair.service;

import finley.gmair.util.ResultData;

import java.util.Map;

/**
 * @author: Bright Chan
 * @date: 2020/7/26 11:24
 * @description: TODO
 */
public interface MachineEfficientFilterService {

    ResultData fetch(Map<String, Object> condition);

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
}
