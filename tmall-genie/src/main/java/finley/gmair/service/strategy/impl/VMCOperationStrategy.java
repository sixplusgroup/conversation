package finley.gmair.service.strategy.impl;

import finley.gmair.service.MachineService;
import finley.gmair.service.holder.HandlerOperationType;
import finley.gmair.service.impl.CommonServiceImpl;
import finley.gmair.service.strategy.OperationStrategy;
import finley.gmair.util.ResultData;
import finley.gmair.util.tmall.TmallDeviceTypeEnum;

import finley.gmair.util.tmall.TmallModeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@HandlerOperationType(TmallDeviceTypeEnum.VMC)
public class VMCOperationStrategy implements OperationStrategy {

    @Autowired
    private MachineService machineService;

    @Autowired
    private CommonServiceImpl commonServiceImpl;

    @Override
    public ResultData turnOn(String deviceId) {
        return machineService.chooseComponent(deviceId, "power", "on");
    }

    @Override
    public ResultData turnOff(String deviceId) {
        return machineService.chooseComponent(deviceId, "power", "off");
    }

    @Override
    public ResultData setWindSpeed(String deviceId, String value, boolean up, boolean down) {
        // value 1 ~ 4，表示风速 1 - 4档
        int speed = commonServiceImpl.getSpeedByValue(deviceId, value, up, down);
        return speed == CommonServiceImpl.CAN_NOT_CONFIG_SPEED ? null : machineService.configSpeed(deviceId, speed);
    }

    @Override
    public ResultData openSwing(String deviceId) {
        // 不支持摆风
        return null;
    }

    @Override
    public ResultData closeSwing(String deviceId) {
        // 不支持摆风
        return null;
    }

    @Override
    public ResultData setMode(String deviceId, String value) {
        ResultData resultData = new ResultData();
        TmallModeEnum modeEnum = TmallModeEnum.valueOf(value);
        switch (modeEnum) {
            case sleep:
                resultData = machineService.chooseComponent(deviceId, "mode", "sleep");
                break;
            case manual:
                resultData = machineService.chooseComponent(deviceId, "mode", "manual");
                break;
            case auto:
                resultData = machineService.chooseComponent(deviceId, "mode", "auto");
                break;
        }
        return resultData;
    }

}