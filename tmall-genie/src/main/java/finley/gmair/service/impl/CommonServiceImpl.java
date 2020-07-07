package finley.gmair.service.impl;

import finley.gmair.model.tmallGenie.Attribute;
import finley.gmair.service.MachineService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class CommonServiceImpl {

    // 风速最低档
    private static final int MIN_GEAR = 1;

    // 风速最高档
    private static final int MAX_GEAR = 4;

    public static final int CAN_NOT_CONFIG_SPEED = -1;

    // 冷风
    private static final String COLD = "cold";

    @Autowired
    private MachineService machineService;

    public List<Attribute> getProperties(String deviceId, String modelId) {
        List<Attribute> properties = new ArrayList<>();
        ResultData runningStatus = machineService.runningStatus(deviceId);
        LinkedHashMap<String, Object> runningInfo = (LinkedHashMap<String, Object>) runningStatus.getData();
        if (runningStatus.getResponseCode() == ResponseCode.RESPONSE_OK && runningInfo != null) {
            // Query请求电源状态(powerstate)必须返回，其他属性的返回与否视设备自身情况而定
            int p = (int) runningInfo.get("power");
            String power = getPower(p);
            if (power != null) {
                properties.add(new Attribute("powerstate", power));
                // 档位
                String windspeed = getWindspeedByVolume((int) runningInfo.get("volume"), modelId);
                properties.add(new Attribute("windspeed", windspeed));
                properties.add(new Attribute("temperature", Integer.toString((int) runningInfo.get("temperature"))));
            }
        }
        return properties;
    }

    public String getPower(int p) {
        String power;
        if (p == 0) {
            power = "off";
        } else if (p == 1) {
            power = "on";
        } else {
            power = null;
        }
        return power;
    }

    public String setResponseName(String name) {
        return name.concat("Response");
    }

    public String getWindspeedByVolume(int volume, String modelId) {
        // 根据型号获取型号具体信息
        ResultData resultData = machineService.probeModelVolumeByModelId(modelId);
        List<LinkedHashMap<String, Object>> modelLists = (List<LinkedHashMap<String, Object>>) resultData.getData();

        if (modelLists != null) {
            for (LinkedHashMap<String, Object> modelInfo : modelLists) {
                if (modelInfo.get("configMode").equals("cold")) {

                    int minVolume = Integer.parseInt(modelInfo.get("minVolume").toString());
                    int maxVolume = Integer.parseInt(modelInfo.get("maxVolume").toString());

                    // 间隔，舍掉
                    int gap = (int) Math.floor((maxVolume - minVolume) / 3.0);
                    // 档位，步长为1，是指1档跳成2档这样
                    return getGearBySpeed(minVolume, gap, volume);
                }
            }
        }
        return null;
    }

    private String getGearBySpeed(int minVolume, int gap, int speed) {
        int initVolume = minVolume;
        for (int i = 1; i <= 4; i++) {
            if (speed >= initVolume && speed < initVolume + gap) {
                return String.valueOf(i);
            } else {
                initVolume += gap;
            }
        }
        return null;
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
    public int getSpeedByValue(String codeValue, String value, boolean up, boolean down) {
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
