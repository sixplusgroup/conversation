package finley.gmair.util.tmall;

/**
 * 查询类（与AliGenie.Iot.Device.Query对应）
 *
 * @author TangXin
 * @see <a href="https://www.yuque.com/qw5nze/ga14hc/rftwyo#W2cqR"></a>
 */
public enum TmallQuery {
    // 查询所有标准属性，详情见各个属性
    Query,
    // 查询颜色，Red、Yellow、Blue、White、Black等值（AliGenie以这些值为准，厂家适配）
    QueryColor,
    // 查询电源开关，on(打开)、off(关闭)
    QueryPowerState,
    // 查询温度，返回数值(AliGenie默认的单位为摄氏度，厂家适配该单位)
    QueryTemperature,
    // 查询湿度，返回数值
    QueryHumidity,
    // 查询风速，返回值参考风速控制中的风速值对应表章节 2.2.8.1
    QueryWindSpeed,
    // 查询亮度，返回数值
    QueryBrightness,
    // 查询雾量，返回数值
    QueryFog,
    // 查询模式，返回值枚举参考例子模式切换中的例子
    QueryMode,
    // 查询pm2.5含量，返回数值
    QueryPM25,
    // 查询方向，返回 left,right,forward,back,up,down
    QueryDirection,
    // 查询角度，返回数值，单位度
    QueryAngle
}
