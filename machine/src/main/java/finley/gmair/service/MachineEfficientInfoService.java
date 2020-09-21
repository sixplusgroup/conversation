package finley.gmair.service;

/**
 * @author: Bright Chan
 * @date: 2020/9/14 21:42
 * @description: MachineEfficientInfoService
 */

public interface MachineEfficientInfoService {

    long getSubSti(String qrcode);

    long getRunning(String qrcode);

    int getAbnormal(String qrcode);
}
