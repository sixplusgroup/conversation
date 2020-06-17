package finley.gmair.utils;

import java.util.Calendar;

/**
 * @ClassName: DAUtils
 * @Description: TODO
 * @Author fan
 * @Date 2020/5/19 2:30 PM
 */
public class DAUtils {
    public static long HOUR_INTERVAL_IN_SECONDS = 60 * 60;

    public static long DAY_INTERVAL_IN_SECONDS = 24 * HOUR_INTERVAL_IN_SECONDS;

    public static long dayEntry() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime().getTime();
    }

    public static long hourEntry() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime().getTime();
    }
}
