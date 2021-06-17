package finley.gmair.util;

/**
 * @author fan
 * @date 2019/5/12 5:38 PM
 */
public class TimeUtil {

    /**
     * 时间1是否超过时间2某个差额
     *
     * @param t1 时间1，单位毫秒
     * @param t2 时间2，单位毫秒
     * @param duration 差额，单位秒
     * @return 时间t1 超过 时间t2 是否大于 差额时间duration
     */
    public static boolean exceed(long t1, long t2, int duration) {
        return Math.abs(t1 - t2) / 1000 > duration;
    }
}
