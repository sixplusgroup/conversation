package finley.gmair.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateFormatUtil {
    public static LocalDate convertToLocalDate(String dateString) {
        if (dateString == null) {
            return null;
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("y-M-d");
        try {
            LocalDate localDate = LocalDate.parse(dateString, dateTimeFormatter);
            return localDate;
        } catch (Exception e) {

        }
        return null;
    }
}
