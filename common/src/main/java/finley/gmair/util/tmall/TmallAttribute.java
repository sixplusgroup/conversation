package finley.gmair.util.tmall;

/**
 * @see <a href="https://www.yuque.com/qw5nze/ga14hc/rftwyo#e6367251"></a>
 */
public enum TmallAttribute {

    // 电源状态 on(打开) off(关闭)
    powerstate("powerstate"),

    // 颜色
    color("color"),

    // 温度 摄氏度
    temperature("temperature"),

    // 风速 档
    windspeed("windspeed"),

    // 亮度
    brightness("brightness"),

    // 雾量
    fog("fog"),

    // 湿度
    humidity("humidity"),

    // pm2.5
    pm2_5("pm2.5"),

    // 电视频道 标准的频道名称
    channel("channel"),

    // 电视频道号
    number("number"),

    // 方向 取值left,right,forward,back,up,down
    direction("direction"),

    // 角度 度
    angle("angle"),

    // 负离子功能 on(打开) off(关闭)
    anion("anion"),

    // 出水功能 on(打开) off(关闭)
    effluent("effluent"),

    // 模式 参考mode设置中的mode取值表
    mode("mode"),

    // 剩余时间
    lefttime("lefttime"),

    // 设备远程状态 on(打开) off(关闭)
    remotestatus("remotestatus"),

    // 设备在线状态 online(在线) offline(离线)
    onlinestate("onlinestate");

    private String attribute;

    TmallAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getAttribute() {
        return attribute;
    }

    @Override
    public String toString() {
        return attribute;
    }
}
