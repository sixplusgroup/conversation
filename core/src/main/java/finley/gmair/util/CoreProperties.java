package finley.gmair.util;

import java.io.InputStream;
import java.util.Properties;

public class CoreProperties {
    private static Properties props = new Properties();

    static {
        InputStream input = CoreProperties.class.getClassLoader().getResourceAsStream("netty-server.properties");
        try {
            props.load(input);
        } catch (Exception e) {

        }
    }

    private CoreProperties() {
        super();
    }

    public static String getValue(String key) {
        return props.getProperty(key);
    }
}
