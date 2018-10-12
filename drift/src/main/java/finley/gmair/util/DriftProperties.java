package finley.gmair.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

public class DriftProperties {
    private Logger logger = LoggerFactory.getLogger(DriftProperties.class);

    private static Properties props = new Properties();

    static {
        InputStream input = DriftProperties.class.getClassLoader().getResourceAsStream("drift.properties");
        try {
            props.load(input);
        } catch (Exception e) {

        }
    }

    private DriftProperties() {
        super();
    }

    public static String getValue(String key) {
        return props.getProperty(key);
    }
}
