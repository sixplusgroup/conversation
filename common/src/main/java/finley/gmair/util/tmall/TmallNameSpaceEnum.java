package finley.gmair.util.tmall;

import java.util.HashMap;
import java.util.Map;

/**
 * header协议中的namespace列表
 *
 * @author TangXin
 * @see <a href="https://www.yuque.com/qw5nze/ga14hc/rftwyo#VuTzZ"></a>
 */
public enum TmallNameSpaceEnum {

    // 设备发现
    DISCOVERY("AliGenie.Iot.Device.Discovery"),

    // 设备控制
    CONTROL("AliGenie.Iot.Device.Control"),

    // 设备属性查询
    QUERY("AliGenie.Iot.Device.Query");

    private String namespace;

    TmallNameSpaceEnum(String namespace) {
        this.namespace = namespace;
    }

    public String getNamespace() {
        return namespace;
    }

    // Implementing a fromString method on an enum type
    private static final Map<String, TmallNameSpaceEnum> stringToEnum = new HashMap<>();

    static {
        // Initialize map from constant name to enum constant
        for (TmallNameSpaceEnum nameSpace : values()) {
            stringToEnum.put(nameSpace.toString(), nameSpace);
        }
    }

    // Returns NameSpace for string, or null if string is invalid
    public static TmallNameSpaceEnum fromString(String symbol) {
        return stringToEnum.get(symbol);
    }

    @Override
    public String toString() {
        return namespace;
    }

}
