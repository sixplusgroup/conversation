package finley.gmair.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimeUtil {
    public static long timestampDiff(long l1, long l2) {
        return (l1 >= l2) ? l1 - l2 : l2 - l1;
    }

    public static String getCurrentTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Calendar calendar = Calendar.getInstance();
        String time = sdf.format(calendar.getTime());
        return time;
    }
}
