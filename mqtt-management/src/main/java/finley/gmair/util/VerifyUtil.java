package finley.gmair.util;

import finley.gmair.exception.MqttBusinessException;

/**
 * controller的校验工具
 *
 * @author lycheeshell
 * @date 2020/12/12 16:04
 */
public class VerifyUtil {

    /**
     * 校验条件是否成立，不成立则抛出异常
     *
     * @param condition 校验条件
     * @param message   异常的信息
     * @throws MqttBusinessException 校验条件不成立,抛出异常
     */
    public static void verify(boolean condition, String message) throws MqttBusinessException {
        if (!condition) {
            throw new MqttBusinessException(message);
        }
    }

}
