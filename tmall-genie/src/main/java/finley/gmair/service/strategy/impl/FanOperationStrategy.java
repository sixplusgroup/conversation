package finley.gmair.service.strategy.impl;

import finley.gmair.service.holder.HandlerOperationType;
import finley.gmair.service.MachineService;
import finley.gmair.service.strategy.OperationStrategy;
import finley.gmair.util.ResultData;
import finley.gmair.util.tmall.TmallDeviceTypeEnum;

import finley.gmair.util.tmall.TmallModeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

@Service
@HandlerOperationType(TmallDeviceTypeEnum.fan)
public class FanOperationStrategy implements OperationStrategy {

    // 风速最低档
    private static final int MIN_GEAR = 1;

    // 风速最高档
    private static final int MAX_GEAR = 4;

    private static final int CAN_NOT_CONFIG_SPEED = -1;

    // 冷风
    private static final String COLD = "cold";

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
        return speed == CAN_NOT_CONFIG_SPEED ? null : machineService.configSpeed(deviceId, speed);
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

    /**
     * 获取应该调整的风速
     *
     * @param codeValue 二维码
     * @param value     可能是1 ~ 4，也可以是min，max
     * @param up        调高风速
     * @param down      调低风速
     * @return 调整后的风速
     */
    @SuppressWarnings("unchecked")
    private int getSpeedByValue(String codeValue, String value, boolean up, boolean down) {
        // 根据二维码获取型号
        ResultData response = machineService.getModel(codeValue);
        List<LinkedHashMap<String, Object>> goodsModelList = (List<LinkedHashMap<String, Object>>) response.getData();

        if (goodsModelList != null && goodsModelList.size() != 0) {
            // 根据型号获取型号具体信息
            String modelId = (String) goodsModelList.get(0).get("modelId");
            ResultData resultData = machineService.probeModelVolumeByModelId(modelId);
            List<LinkedHashMap<String, Object>> modelLists = (List<LinkedHashMap<String, Object>>) resultData.getData();
            if (modelLists != null) {
                for (LinkedHashMap<String, Object> modelInfo : modelLists) {
                    if (modelInfo.get("configMode").equals(COLD)) {

                        int minVolume = Integer.parseInt(modelInfo.get("minVolume").toString());
                        int maxVolume = Integer.parseInt(modelInfo.get("maxVolume").toString());

                        // 间隔，舍掉
                        int gap = (int) Math.floor((maxVolume - minVolume) / 3.0);
                        // 风速
                        int speed;
                        // 档位，步长为1，是指1档跳成2档这样
                        int gear;
                        switch (value) {
                            case "max":
                                speed = maxVolume;
                                gear = MAX_GEAR;
                                break;
                            case "min":
                                speed = minVolume;
                                gear = MIN_GEAR;
                                break;
                            default:
                                gear = Integer.parseInt(value);
                                speed = minVolume + gap * (gear - 1);
                        }

                        // 决定是否调整风速
                        if (up) {
                            speed += (gear < MAX_GEAR ? gap : 0);
                        } else if (down) {
                            speed -= (gear > MIN_GEAR ? gap : 0);
                        }

                        return speed;
                    }
                }
            }
        }

        return CAN_NOT_CONFIG_SPEED;
    }

}
