package finley.gmair.util;

public class LocationUtil {
    public static boolean isMunicipality(String code) {
        if ("110000".equals(code) || "120000".equals(code) || "310000".equals(code) || "500000".equals(code)) {
            return true;
        }
        return false;
    }
}
