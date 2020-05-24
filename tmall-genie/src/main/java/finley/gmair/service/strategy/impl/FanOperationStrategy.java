package finley.gmair.service.strategy.impl;

import finley.gmair.service.holder.HandlerOperationType;
import finley.gmair.service.MachineService;
import finley.gmair.service.strategy.OperationStrategy;
import finley.gmair.util.ResultData;
import finley.gmair.util.tmall.TmallDeviceType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@HandlerOperationType(TmallDeviceType.fan)
public class FanOperationStrategy implements OperationStrategy {

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
        int speed = getSpeedByValue(value, up, down);
        return machineService.configSpeed(deviceId, speed);
    }

    @Override
    public ResultData openSwing(String deviceId) {
        return machineService.chooseComponent(deviceId, "sweep", "on");
    }

    @Override
    public ResultData closeSwing(String deviceId) {
        return machineService.chooseComponent(deviceId, "sweep", "off");
    }

    private int getSpeedByValue(String value, boolean up, boolean down) {
        // TODO 具体范围到数据库中查找
        // 根据up/down增速/减速
        int speed;
        switch (value) {
            case "max":
                speed = 80;
                break;
            case "min":
                speed = 20;
                break;
            default:
                speed = Integer.parseInt(value) * 20;
        }
        return speed;
    }

}
