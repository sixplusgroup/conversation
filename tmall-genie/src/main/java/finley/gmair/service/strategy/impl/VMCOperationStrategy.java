package finley.gmair.service.strategy.impl;

import finley.gmair.service.MachineService;
import finley.gmair.service.holder.HandlerOperationType;
import finley.gmair.service.strategy.OperationStrategy;
import finley.gmair.util.ResultData;
import finley.gmair.util.tmall.TmallDeviceType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@HandlerOperationType(TmallDeviceType.VMC)
public class VMCOperationStrategy implements OperationStrategy {

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
        return null;
    }

    @Override
    public ResultData openSwing(String deviceId) {
        return null;
    }

    @Override
    public ResultData closeSwing(String deviceId) {
        return null;
    }

}
