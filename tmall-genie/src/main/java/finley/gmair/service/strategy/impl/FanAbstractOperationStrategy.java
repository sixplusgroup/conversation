package finley.gmair.service.strategy.impl;

import finley.gmair.service.holder.HandlerOperationType;
import finley.gmair.service.MachineService;
import finley.gmair.service.strategy.OperationStrategy;
import finley.gmair.util.ResultData;
import finley.gmair.util.tmall.TmallDeviceTypeEnum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

@Service
@HandlerOperationType(TmallDeviceTypeEnum.fan)
public class FanAbstractOperationStrategy implements OperationStrategy {

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
        int speed = getSpeedByValue(deviceId, value, up, down);
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

    @SuppressWarnings("unchecked")
    private int getSpeedByValue(String codeValue, String value, boolean up, boolean down) {

        // 根据up/down增速/减速
        int speed = Integer.parseInt(value);

        ResultData response = machineService.getModel(codeValue);
        List<LinkedHashMap<String, Object>> goodsModelList = (List<LinkedHashMap<String, Object>>) response.getData();

        if (goodsModelList != null && goodsModelList.size() != 0) {
            LinkedHashMap<String, Object> hashMap = goodsModelList.get(0);
            String modelId = (String) hashMap.get("modelId");
            ResultData resultData = machineService.probeModelVolumeByModelId(modelId);
            List<LinkedHashMap<String, Object>> list = (List<LinkedHashMap<String, Object>>) resultData.getData();

            if (list != null) {
                hashMap = list.get(0);
                System.out.println(hashMap.get("minVolume"));
                int minVolume = Integer.parseInt(hashMap.get("minVolume").toString());
                int maxVolume = Integer.parseInt(hashMap.get("maxVolume").toString());

                switch (value) {
                    case "max":
                        speed = maxVolume;
                        break;
                    case "min":
                        speed = minVolume;
                        break;
                    default:
                        // TODO 根据volumtable每次跳一个值
                        if (up) {
                            speed = minVolume + (maxVolume - minVolume) / 3 * (speed < 4 ? speed : speed - 1);
                        } else if (down) {
                            speed = minVolume + (maxVolume - minVolume) / 3 * (speed > 1 ? speed - 2 : speed - 1);
                        } else {
                            speed = minVolume + (maxVolume - minVolume) / 3 * (speed - 1);
                        }
                }
                return speed;
            }
        }

        // 给一个默认值
        return speed * 60;
    }

}
