package finley.gmair.util;

import com.alibaba.fastjson.JSONObject;

/**
 * @ClassName: MachineUtil
 * @Description: TODO
 * @Author fan
 * @Date 2019/5/24 12:39 PM
 */
public class MachineUtil {
    public static JSONObject normalize(Object o) {
        if (o instanceof finley.gmair.model.machine.v1.MachineStatus) {
            return normalize((finley.gmair.model.machine.v1.MachineStatus) o);
        }
        if (o instanceof finley.gmair.model.machine.MachineStatus) {
            return normalize((finley.gmair.model.machine.MachineStatus) o);
        }
        if (o instanceof finley.gmair.model.machine.v3.MachineStatusV3) {
            return normalize((finley.gmair.model.machine.v3.MachineStatusV3) o);
        }
        return null;
    }

    private static JSONObject normalize(finley.gmair.model.machine.v1.MachineStatus status) {
        JSONObject json = new JSONObject();
        json.put("uid", status.getUid());
        json.put("pm2_5", status.getPm2_5());
        json.put("temperature", status.getTemp());
        json.put("humidity", status.getHumid());
        json.put("volume", status.getVolume());
        json.put("power", status.getPower());
        json.put("mode", status.getMode());
        json.put("heat", status.getHeat());
        json.put("light", status.getLight());
        return json;
    }

    private static JSONObject normalize(finley.gmair.model.machine.MachineStatus status) {
        JSONObject json = new JSONObject();
        json.put("uid", status.getUid());
        json.put("pm2_5", status.getPm2_5());
        json.put("temperature", status.getTemp());
        json.put("humidity", status.getHumid());
        json.put("co2", status.getCo2());
        json.put("volume", status.getVolume());
        json.put("power", status.getPower());
        json.put("mode", status.getMode());
        json.put("heat", status.getHeat());
        json.put("light", status.getLight());
        json.put("lock", status.getLock());
        return json;
    }

    private static JSONObject normalize(finley.gmair.model.machine.v3.MachineStatusV3 status) {
        JSONObject json = new JSONObject();
        json.put("uid", status.getUid());
        json.put("pm2_5", status.getPm2_5a());
        json.put("temperature", status.getTempIndoor());
        json.put("humidity", status.getHumidity());
        json.put("co2", status.getCo2());
        json.put("volume", status.getVolume());
        json.put("power", status.getPower());
        json.put("mode", status.getMode());
        json.put("heat", status.getHeat());
        json.put("light", status.getLight());
        json.put("lock", status.getChildlock());
        return json;
    }
}