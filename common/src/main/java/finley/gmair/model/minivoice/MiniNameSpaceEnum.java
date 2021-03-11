package finley.gmair.model.minivoice;

import java.util.HashMap;
import java.util.Map;

public enum  MiniNameSpaceEnum {

    // 天气查询
    WEATHER("Gmair.Weather.Query"),

    // 设备控制
    CONTROL("Gmair.Iot.Device.Control"),

    // 设备属性查询
    QUERY("Gmair.Iot.Device.Query");

    private String namespace;

    MiniNameSpaceEnum(String namespace) {
        this.namespace = namespace;
    }

    public String getNamespace() {
        return namespace;
    }

    // Implementing a fromString method on an enum type
    private static final Map<String, MiniNameSpaceEnum> stringToEnum = new HashMap<>();

    static {
        // Initialize map from constant name to enum constant
        for (MiniNameSpaceEnum nameSpace : values()) {
            stringToEnum.put(nameSpace.toString(), nameSpace);
        }
    }

    // Returns NameSpace for string, or null if string is invalid
    public static MiniNameSpaceEnum fromString(String symbol) {
        return stringToEnum.get(symbol);
    }

    @Override
    public String toString() {
        return namespace;
    }

}
