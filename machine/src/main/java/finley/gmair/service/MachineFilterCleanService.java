package finley.gmair.service;

import finley.gmair.model.machine.MachineFilterClean;
import finley.gmair.util.ResultData;

import java.util.Map;

/**
 * @author: Bright Chan
 * @date: 2020/7/4 13:31
 * @description: TODO
 */
public interface MachineFilterCleanService {

    ResultData fetch(Map<String, Object> condition);

    ResultData fetchByQRCode(String qrcode);

    ResultData modify(Map<String, Object> condition);

    /**
     * 判断被选中的设备是否需要清洗
     * 判断逻辑：查询MachineFilterClean表中对应字段，若该字段中的isNeedClean属性为true，
     * 则直接判断为需要清洗；若为false，则将lastConfirmTime与请求时间做对比，若相差大于等于30天，
     * 则判断为需要清洗，否则判断为不需要清洗。
     * @param selectedOne 被选中的设备的二维码
     * @return 判断结果
     */
    ResultData filterCleanCheck(MachineFilterClean selectedOne);

    /**
     * 新增绑定用户的时候调用此方法新增machine_filter_clean表数据
     * @param qrcode 新增绑定的二维码
     * @return 新增结果
     */
    ResultData addNewBindMachine(String qrcode);
}
