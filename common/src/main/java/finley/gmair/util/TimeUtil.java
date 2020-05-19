package finley.gmair.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {
    public static long timestampDiff(long l1, long l2) {
        return (l1 >= l2) ? l1 - l2 : l2 - l1;
    }

    public static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Calendar calendar = Calendar.getInstance();
        String time = sdf.format(calendar.getTime());
        return time;
    }

    //获取当前小时整点时间戳
    public static Timestamp getCurrentHourTimestamp() {
        Timestamp lastHour = new Timestamp((System.currentTimeMillis()) / (1000 * 60 * 60) * (1000 * 60 * 60));
        return lastHour;
    }

    //传入一个时间戳，转换成那个时间整点的时间戳()
    public static Timestamp getThatTimeStampHourTimestamp(Timestamp timestamp) {
        return new Timestamp(timestamp.getTime() / (1000 * 60 * 60) * (1000 * 60 * 60));
    }

    //获取今天零点时间戳
    public static Timestamp getTodayZeroTimestamp() {
        Long currentTimestamps = System.currentTimeMillis();
        Long oneDayTimestamps = Long.valueOf(60 * 60 * 24 * 1000);
        return new Timestamp(currentTimestamps - (currentTimestamps + 60 * 60 * 8 * 1000) % oneDayTimestamps);
    }

    public static Timestamp getThatTimeStampDayZeroTimestamp(Timestamp timestamp) {
        return new Timestamp(timestamp.getTime() - (timestamp.getTime() + 8 * 60 * 60 * 1000) % (24 * 60 * 60 * 1000));
    }

    //将String类型转化为时间 年月日
    public static Date formatTimeToDate(String str) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try {
            date = sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
