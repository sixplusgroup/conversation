package finley.gmair.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
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
        try {
            return new String(props.getProperty(key).getBytes(StandardCharsets.ISO_8859_1), "gbk");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean contains(String key, String goodsName) {
        return Objects.requireNonNull(getValue(key)).contains(goodsName);
    }

}
