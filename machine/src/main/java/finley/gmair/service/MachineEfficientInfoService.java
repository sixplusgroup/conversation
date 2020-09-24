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

    /**
     * 返回自上次确认更换到当前的时间（小时）, 用Substi表示
     * @param qrcode 选中的设备qrocde
     * @return 自上次确认更换到当前的时间（小时）
     */
    int getSubsti(String qrcode);

    /**
     * 每小时更新machine_efficient_information表中的running字段
     * @return 执行结果
     */
    ResultData hourlyUpdate();

    /**
     * 每天更新machine_efficient_information表中的conti和abnormal字段
     * @return 执行结果
     */
    ResultData dailyUpdate();
}
