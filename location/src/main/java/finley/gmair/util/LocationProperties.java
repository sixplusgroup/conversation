package finley.gmair.util;

import java.io.InputStream;
import java.util.Properties;

public class LocationProperties {
    private static Properties props = new Properties();

    static {
        InputStream input = LocationProperties.class.getClassLoader().getResourceAsStream("location.properties");
        try {
            props.load(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private LocationProperties() {
        super();
    }

    public static String getValue(String key) {
        return props.getProperty(key);
    }
}
