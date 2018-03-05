package finley.gmair.util;

import java.io.InputStream;
import java.util.Properties;

public class MessageProperties {
    private static Properties props = new Properties();

    static {
        InputStream input = MessageProperties.class.getClassLoader().getResourceAsStream("message.properties");
        try {
            props.load(input);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private MessageProperties() { super(); }

    public static String getValue(String key) {
        return props.getProperty(key);
    }
}
