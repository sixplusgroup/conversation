package finley.gmair.util;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExpUtil {

    public static Timestamp extractDateTime(String text) {
        Timestamp t = new Timestamp(System.currentTimeMillis());
        String pattern = ".*?(\\d{4}[-]\\d{2}[-]\\d{2}\\s\\d{2}[:]\\d{2}[:]\\d{2}).*";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(text);
        if (m.find( )) {
            String timeString = m.group(1);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("y-M-d H:m:s");
            LocalDateTime localDateTime = LocalDateTime.parse(timeString, formatter);
            t = Timestamp.valueOf(localDateTime);
        } else {
            System.out.println("NO MATCH");
        }
        return t;
    }


}
