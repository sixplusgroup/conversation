package finley.gmair.util;

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
        StringBuffer sb = new StringBuffer();
        return sb.append("/client/FA/").append(uid).append("/").append(action).toString();
    }

}
