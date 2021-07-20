package finley.gmair.scene.constant;

/**
 * @author : Lyy
 * @create : 2020-12-08 19:26
 * @description 对设备操作类型的枚举
 **/
public enum OperateType {

    SPEED("speed"),
    LIGHT("light"),
    TEMP("temp"),
    TIMING("timing"),
    VOLUME("volume"),
    HEAT("heat"),
    MODE("mode"),
    POWER("power"),
    LOCK("lock");

    private final String operate;

    // 构造器默认也只能是private, 从而保证构造函数只能在内部使用
    OperateType(String operate) {
        this.operate = operate;
    }

    public String getOperate() {
        return this.operate;
    }

}
