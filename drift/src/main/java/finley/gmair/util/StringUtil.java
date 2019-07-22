package finley.gmair.util;

import org.springframework.util.StringUtils;

/**
 * @ClassName: StringUtil
 * @Description: TODO
 * @Author fan
 * @Date 2019/7/15 11:08 AM
 */
public class StringUtil {
    public static boolean isEmpty(String... args) {
        for (String item : args) {
            if (StringUtils.isEmpty(item)) return false;
        }
        return true;
    }
}
