package finley.gmair.util;

import java.io.InputStream;
import java.util.Properties;

public class ManagementConfig {
    private static Properties props = new Properties();

    static {
        InputStream input = ManagementConfig.class.getClassLoader().getResourceAsStream("management.properties");
        try {
            props.load(input);
        } catch (Exception e) {

        }
    }

    private ManagementConfig() {
        super();
    }

    public static String getValue(String key) {
        return props.getProperty(key);
    }
}