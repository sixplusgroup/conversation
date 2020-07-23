package finley.gmair.service;

import finley.gmair.util.ResultData;

import java.util.Map;

/**
 * @author: Bright Chan
 * @date: 2020/7/18 15:36
 * @description: TODO
 */
public interface MachineTurboVolumeService {

    /**
     * 当新增绑定用户的时候，如果用户设备为指定型号且尚未记录在machine_turbo_volume表中，
     * 则在machine_turbo_volume表中新增一条记录。
     * @param qrcode
     * @return
     */
    ResultData create(String qrcode);

    ResultData modify(Map<String, Object> condition);

    ResultData fetch(Map<String, Object> condition);

    ResultData fetchByQRCode(String qrcode);

    ResultData getTurboVolumeValue(String qrcode);
}
