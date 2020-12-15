package finley.gmair.mqtt;

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

}
