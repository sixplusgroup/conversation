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
@HandlerOperationType(TmallDeviceTypeEnum.fan)
public class FanOperationStrategy implements OperationStrategy {

    @Autowired
    private CommonServiceImpl commonServiceImpl;

    @Autowired
    private MachineService machineService;

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
        return machineService.chooseComponent(deviceId, "sweep", "on");
    }

    @Override
    public ResultData closeSwing(String deviceId) {
        return machineService.chooseComponent(deviceId, "sweep", "off");
    }

    @Override
    public ResultData setMode(String deviceId, String value) {
        ResultData resultData = new ResultData();
        TmallModeEnum modeEnum = TmallModeEnum.valueOf(value);
        switch (modeEnum) {
            case sleep:
                resultData = machineService.chooseComponent(deviceId, "mode", "sleep");
                break;
            case natureWind:
                resultData = machineService.chooseComponent(deviceId, "mode", "normal");
                break;
            case power:
                resultData = machineService.chooseComponent(deviceId, "mode", "strong");
                break;
            case hygiene:
                // 除菌 → 净化
                resultData = machineService.chooseComponent(deviceId, "mode", "pure");
                break;
        }
        return resultData;
    }

}
