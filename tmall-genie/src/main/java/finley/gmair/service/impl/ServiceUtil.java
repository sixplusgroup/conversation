package finley.gmair.service.impl;

import finley.gmair.model.tmallGenie.Attribute;
import finley.gmair.model.tmallGenie.Header;
import finley.gmair.service.MachineService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class ServiceUtil {

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

}
