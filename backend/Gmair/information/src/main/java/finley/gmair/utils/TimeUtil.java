package finley.gmair.utils;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class TimeUtil {

    public final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * long转换成字符串日期
     * @return yyyy-MM-dd HH:mm:ss
     */
    public String getDateTime(long longTime) {
        return dateToString(new Date(longTime), dateFormat);
    }

    /**
     * 日期类型转换成字符串类型
     * @param date 日期
     * @param dateFormat 日期格式
     * @return 日期字符串
     */
    public String dateToString(Date date, DateFormat dateFormat) {
        return dateFormat.format(date);
    }

}