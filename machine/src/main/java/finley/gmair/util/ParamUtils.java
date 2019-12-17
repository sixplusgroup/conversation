package finley.gmair.util;

import org.springframework.util.StringUtils;

/**
 * @ClassName: ParamUtils
 * @Description: TODO
 * @Author fan
 * @Date 2019/12/8 10:12 PM
 */
public class ParamUtils {
    public static boolean containEmpty(Object... args) {
        for (Object s : args) {
            if (StringUtils.isEmpty(s)) return true;
        }
        return false;
    }
}
