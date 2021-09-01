package finley.gmair.mqtt;

import com.alibaba.fastjson.JSONObject;
import finley.gmair.datastructrue.LimitQueue;
import finley.gmair.pool.CorePool;
import finley.gmair.service.RedisService;

/**
 * mqtt工具类
 *
 * @author lycheeshell
 * @date 2020/12/3 22:22
 */
public class MqttUtil {

    /**
     * 根据uid,action生成相应的topic
     */
    public static String produceTopic(String uid, String action) {
        StringBuilder sb = new StringBuilder();
        sb.append("/client/FA/").append(uid).append("/").append(action);
        return sb.toString();
    }

    /**
     * 发送时间同步主题
     *
     * @param mqttGateway mqtt消息发布网关
     * @param machineId 设备id
     * @return 发布成功
     */
    public static boolean publishTimeSyncTopic(final MqttGateway mqttGateway, final String machineId) {
        String topic = produceTopic(machineId, MqttTopicExtension.SET_TIME);
        JSONObject json = new JSONObject();
        json.put("time", System.currentTimeMillis() / 1000);
        CorePool.getComPool().execute(() -> mqttGateway.sendToMqtt(topic, 2, json.toString()));
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
        if (candidate.containsKey("pm2.5a") && candidate.getIntValue("pm2.5a") != origin.getPm2_5a()) {
            origin.setPm2_5a(candidate.getIntValue("pm2.5a"));
        }
        if (candidate.containsKey("pm2.5b") && candidate.getIntValue("pm2.5b") != origin.getPm2_5b()) {
            origin.setPm2_5b(candidate.getIntValue("pm2.5b"));
        }
        if (candidate.containsKey("temp") && candidate.getIntValue("temp") != origin.getTempIndoor()) {
            origin.setTempIndoor(candidate.getIntValue("temp"));
        }
        if (candidate.containsKey("temp_out") && candidate.getIntValue("temp_out") != origin.getTempOutdoor()) {
            origin.setTempOutdoor(candidate.getIntValue("temp_out"));
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
                if (candidate.containsKey("speed")) {
                    origin.setVolume(candidate.getIntValue("speed"));
                }
            } else {
                if (candidate.containsKey("speed")) {
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
