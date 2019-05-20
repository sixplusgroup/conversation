package finley.gmair.util;

import com.alibaba.fastjson.JSONObject;
import finley.gmair.model.machine.v3.MachineStatusV3;
import finley.gmair.pool.CorePool;
import finley.gmair.service.MqttService;
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
        CorePool.getComPool().execute(() -> {
            service.publish(topic, json);
        });
        return true;
    }
}
