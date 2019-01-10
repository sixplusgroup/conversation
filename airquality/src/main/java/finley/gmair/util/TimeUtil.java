package finley.gmair.util;

public class TimeUtil {
    public static int days(long start, long end) {
        if (start > end) {
            return (int) (start - end) / 1000 / 60 / 60 / 24;
        }
        return (int) (end - start) / 1000 / 60 / 60 / 24;
    }
}
