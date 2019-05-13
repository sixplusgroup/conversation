package finley.gmair.util;

/**
 * @ClassName: TimeUtil
 * @Description: TODO
 * @Author fan
 * @Date 2019/5/12 5:38 PM
 */
public class TimeUtil {
    public static boolean exceed(long t1, long t2, int duration) {
        return Math.abs(t1 - t2) / 1000 >= duration;
    }
}
