package finley.gmair.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public class GoodsProperties {
    private static Properties props = new Properties();

    static {
        InputStream input = GoodsProperties.class.getClassLoader().getResourceAsStream("goods.properties");
        try {
            props.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private GoodsProperties() {
        super();
    }

    public static String getValue(String key) {
        return props.getProperty(key);
    }

    public static boolean contains(String key, String goodsName) {
        return Objects.requireNonNull(getValue(key)).contains(goodsName);
    }

}
