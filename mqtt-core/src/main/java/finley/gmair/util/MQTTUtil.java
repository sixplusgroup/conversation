package finley.gmair.util;

import finley.gmair.model.machine.v3.MachineStatusV3;
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

    public static MachineStatusV3 merge(MachineStatusV3 origin, MachineStatusV3 current) {
        MachineStatusV3 result = new MachineStatusV3();
        if (StringUtils.isEmpty(current.getCo2())) {
            result.setCo2(origin.getCo2());
        }
        if (StringUtils.isEmpty(current.getHeat())) {
            result.setHeat(origin.getHeat());
        }

        return result;
    }
}
