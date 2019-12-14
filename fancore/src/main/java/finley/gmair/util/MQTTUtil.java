package finley.gmair.util;

import com.alibaba.fastjson.JSONObject;
import finley.gmair.datastructrue.LimitQueue;
import finley.gmair.model.fan.FanStatus;
import finley.gmair.pool.CorePool;
import finley.gmair.service.MqttService;
import finley.gmair.service.RedisService;

/**
 * @ClassName: MQTTUtil
 * @Description: TODO
 * @Author fan
 * @Date 2019/5/11 4:06 PM
 */
public class MQTTUtil {
    /**
     * 根据uid,action生成相应的topic
     */
    public static String produceTopic(String uid, String action) {
        StringBuffer sb = new StringBuffer();
        return sb.append("/client/FA/").append(uid).append("/").append(action).toString();
    }

    public static boolean publishTimeSyncTopic(final MqttService service, final String model, final String mac) {
        String topic = "/GM/FAN/".concat(model).concat("/").concat(mac).concat("/TIMING/SYNC");
        JSONObject json = new JSONObject();
        json.put("mac", mac);
        json.put("timestamp", System.currentTimeMillis());
        CorePool.getComPool().execute(() -> service.publish(topic, json));
        return true;
    }

    public static FanStatus interpret(JSONObject json) {
        if (!json.containsKey("mac")) return null;
        String mac = json.getString("mac");
        int countdown = json.containsKey("countdown") ? json.getIntValue("countdown") : 0;
        int heat = json.containsKey("heat") ? json.getIntValue("heat") : 0;
        int power = json.containsKey("power") ? json.getIntValue("power") : 0;
        int runtime = json.containsKey("runtime") ? json.getIntValue("runtime") : 0;
        int speed = json.containsKey("speed") ? json.getIntValue("speed") : 0;
        int sweep = json.containsKey("sweep") ? json.getIntValue("sweep") : 0;
        int targettemp = json.containsKey("targettemp") ? json.getIntValue("targettemp") : 0;
        int temp = json.containsKey("temp") ? json.getIntValue("temp") : 0;
        int mode = json.containsKey("mode") ? json.getIntValue("mode") : 0;
        return new FanStatus(mac, power, speed, mode, sweep, heat, runtime, countdown, targettemp, temp);
    }
}
