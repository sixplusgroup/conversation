package finley.gmair.service;

import finley.gmair.util.ResultData;

/**
 * @author: Bright Chan
 * @date: 2020/9/14 21:42
 * @description: MachineEfficientInfoService
 */

public interface MachineEfficientInfoService {

    ResultData getSubSti(String qrcode);

    ResultData getRunning(String qrcode);

    int getAbnormal(String qrcode);
}
