package finley.gmair.service;

import finley.gmair.model.machine.MachineEfficientInformation;
import finley.gmair.util.ResultData;

import java.util.Map;

/**
 * @author: Bright Chan
 * @date: 2020/9/14 21:42
 * @description: MachineEfficientInfoService
 */

public interface MachineEfficientInfoService {

    ResultData create(MachineEfficientInformation machineEfficientInformation);

    ResultData fetch(Map<String, Object> condition);

    int getSubsti(String qrcode);

    ResultData hourlyUpdate();

    ResultData dailyUpdate();
}
