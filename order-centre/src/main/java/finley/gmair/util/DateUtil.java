package finley.gmair.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author ：tsl
 * @date ：Created in 2021-12-22 20:49
 * @description ：
 */

public class DateUtil {
    public static void main(String[] args) {
        System.out.println(str2Date("2021-12-13 19:56:06"));
    }

    public static String date2Str(Date date) {
        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sim.format(date);
    }


    public static Date str2Date(String str) {
        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return sim.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
