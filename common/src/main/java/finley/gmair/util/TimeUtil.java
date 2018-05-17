package finley.gmair.util;

public class TimeUtil {
    public static long timestampDiff(long l1, long l2) {
        return (l1 >= l2) ? l1 - l2 : l2 - l1;
    }
}
