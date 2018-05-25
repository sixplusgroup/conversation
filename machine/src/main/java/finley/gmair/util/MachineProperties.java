package finley.gmair.util;

import java.io.InputStream;
import java.util.Properties;

public class MachineProperties {

    private static Properties props = new Properties();
    public static String queueName = "";

    static {
        InputStream input = MachineProperties.class.getClassLoader().getResourceAsStream("rabbitmq.properties");
        try {
            props.load(input);
            queueName = getValue("queue_name");
        } catch (Exception e) {

        }
    }

    private MachineProperties() {
        super();
    }

    public static String getValue(String key) {
        return props.getProperty(key);
    }
}
