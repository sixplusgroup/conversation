package finley.gmair.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

public class ExpressProperties {
    private Logger logger = LoggerFactory.getLogger(ExpressProperties.class);

    private static Properties props = new Properties();

    static {
        InputStream input = ExpressProperties.class.getClassLoader().getResourceAsStream("express.properties");
        try {
            props.load(input);
        } catch (Exception e) {

        }
    }

    private ExpressProperties() {
        super();
    }

    public static String getValue(String key) {
        return props.getProperty(key);
    }
}
