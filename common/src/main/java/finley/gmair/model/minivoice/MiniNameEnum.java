package finley.gmair.model.minivoice;

import java.util.HashMap;
import java.util.Map;

public enum MiniNameEnum {

    // 打开电源
    POWER_ON("PowerOn"),

    // 关闭电源
    POWER_OFF("PowerOff"),

    // 打开辅热
    HEAT_ON("HeatOn"),

    // 设置辅热
    HEAT_SET("HeatSet"),

    // 关闭辅热
    HEAT_OFF("HeatOff"),

    // 设置风速
    SPEED_SET("SpeedSet"),

    // 开启扫风
    SWEEP_ON("SweepOn"),

    // 关闭扫风
    SWEEP_OFF("SweepOff"),

    // 设置模式
    MODE_SET("ModeSet"),

    // 查询所有属性
    QUERY("Query"),

    // 查询电源开关
    QUERY_POWER("QueryPower"),

    // 查询辅热
    QUERY_HEAT("QueryHeat"),

    // 查询扫风
    QUERY_SWEEP("QuerySweep"),

    // 查询风速
    QUERY_SPEED("QuerySpeed"),

    // 查询模式
    QUERY_MODE("QueryMode"),

    // 查询天气实况
    QUERY_CONDITION("QueryCondition"),

    // 查询今天天气预报
    QUERY_TODAY("QueryToday"),

    // 查询明天天气预报
    QUERY_TOMORROW("QueryTomorrow"),

    // 查询未来三天天气预报
    QUERY_THREE("QueryThree"),

    // 查询未来七天天气预报
    QUERY_SEVEN("QuerySeven");

    private String name;

    MiniNameEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    // Implementing a fromString method on an enum type
    private static final Map<String, MiniNameEnum> stringToEnum = new HashMap<>();

    static {
        // Initialize map from constant name to enum constant
        for (MiniNameEnum name : values()) {
            stringToEnum.put(name.toString(), name);
        }
    }

    // Returns NameSpace for string, or null if string is invalid
    public static MiniNameEnum fromString(String symbol) {
        return stringToEnum.get(symbol);
    }

    @Override
    public String toString() {
        return name;
    }

}
