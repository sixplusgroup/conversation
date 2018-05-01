package finley.gmair.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateFormatUtil {
    public static LocalDate convertToLocalDate(String dateString) {
        if (dateString == null) {
            return null;
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("Y-M-D");
        try {
            LocalDateTime localDateTime = LocalDateTime.parse(dateString, dateTimeFormatter);
            return localDateTime.toLocalDate();
        } catch (Exception e) {

        }
        return null;
    }
}
