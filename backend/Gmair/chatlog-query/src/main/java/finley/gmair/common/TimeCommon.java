package finley.gmair.common;

import java.text.SimpleDateFormat;

public interface TimeCommon {
//    String DATE_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Integer ONE_DAY_MILLIS = 24 * 60 * 60 * 1000;
}
