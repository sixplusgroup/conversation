package finley.gmair.util;

import com.alibaba.fastjson.JSONObject;
import finley.gmair.datastructrue.LimitQueue;
import finley.gmair.model.machine.v3.MachineStatusV3;
import finley.gmair.pool.CorePool;
import finley.gmair.service.MqttService;
import finley.gmair.service.RedisService;
import org.springframework.util.StringUtils;

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

    public static boolean publishTimeSyncTopic(final MqttService service, final String machineId) {
        String topic = produceTopic(machineId, TopicExtension.SET_TIME);
        JSONObject json = new JSONObject();
        json.put("time", System.currentTimeMillis() / 1000);
        CorePool.getComPool().execute(() -> service.publish(topic, json));
        return true;
    }

    public static void partial(RedisService redisService, String uid, JSONObject json) {
        if (redisService.exists(uid)) {
            LimitQueue<finley.gmair.model.machine.v3.MachineStatusV3> queue = (LimitQueue<finley.gmair.model.machine.v3.MachineStatusV3>) redisService.get(uid);
            finley.gmair.model.machine.v3.MachineStatusV3 last = queue.getLast();
            last = merge(last, json);
            queue.replaceLast(last);
            redisService.set(uid, queue, (long) 120);
        }
    }

    private static finley.gmair.model.machine.v3.MachineStatusV3 merge(finley.gmair.model.machine.v3.MachineStatusV3 origin, JSONObject candidate) {
        if (candidate.containsKey("pm2_5a") && candidate.getIntValue("pm2_5a") != origin.getPm2_5a()) {
            origin.setPm2_5a(candidate.getIntValue("pm2_5a"));
        }
        if (candidate.containsKey("pm2_5b") && candidate.getIntValue("pm2_5b") != origin.getPm2_5b()) {
            origin.setPm2_5b(candidate.getIntValue("pm2_5b"));
        }
        if (candidate.containsKey("tempIndoor") && candidate.getIntValue("tempIndoor") != origin.getTempIndoor()) {
            origin.setTempIndoor(candidate.getIntValue("tempIndoor"));
        }
        if (candidate.containsKey("tempOutdoor") && candidate.getIntValue("tempOutdoor") != origin.getTempOutdoor()) {
            origin.setTempOutdoor(candidate.getIntValue("tempOutdoor"));
        }
        if (candidate.containsKey("humidity") && candidate.getIntValue("humidity") != origin.getHumidity()) {
            origin.setHumidity(candidate.getIntValue("humidity"));
        }
        if (candidate.containsKey("co2")) {
            origin.setCo2(candidate.getIntValue("co2"));
        }
        //电源关闭情况下不显示风量数值
        if (candidate.containsKey("power")) {
            origin.setPower(candidate.getIntValue("power"));
            if (candidate.getIntValue("power") == 1) {
                if (candidate.containsKey("volume")) {
                    origin.setVolume(candidate.getIntValue("volume"));
                }
            } else {
                if (candidate.containsKey("volume")) {
                    origin.setVolume(0);
                }
            }
        }
        if (candidate.containsKey("mode")) {
            origin.setMode(candidate.getIntValue("mode"));
        }
        if (candidate.containsKey("heat")) {
            origin.setHeat(candidate.getIntValue("heat"));
        }
        if (candidate.containsKey("light")) {
            origin.setLight(candidate.getIntValue("light"));
        }
        if (candidate.containsKey("childlock")) {
            origin.setChildlock(candidate.getIntValue("childlock"));
        }
        return origin;
    }
}
