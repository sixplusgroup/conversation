package finley.gmair.mqtt;

import com.alibaba.fastjson.JSONObject;
import finley.gmair.pool.CorePool;

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

}
