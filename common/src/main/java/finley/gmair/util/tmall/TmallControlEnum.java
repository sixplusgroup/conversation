package finley.gmair.util.tmall;

/**
 * 操作类（与AliGenie.Iot.Device.Control对应）
 *
 * @author TangXin
 * @see <a href="https://www.yuque.com/qw5nze/ga14hc/rftwyo#OKLBv></a>
 */
public enum TmallControlEnum {
    // 打开
    TurnOn,
    // 关闭
    TurnOff,
    // 频道切换
    SelectChannel,
    // 频道增加
    AdjustUpChannel,
    // 频道减少
    AdjustDownChannel,
    // 声音按照步长调大
    AdjustUpVolume,
    // 声音按照步长调小
    AdjustDownVolume,
    // 声音调到某个值
    SetVolume,
    // 设置静音
    SetMute,
    // 取消静音
    CancelMute,
    // 播放
    Play,
    // 暂停
    Pause,
    // 继续
    Continue,
    // 下一首或下一台
    Next,
    // 上一首或上一台
    Previous,
    // 设置亮度
    SetBrightness,
    // 调大亮度
    AdjustUpBrightness,
    // 调小亮度
    AdjustDownBrightness,
    // 设置温度
    SetTemperature,
    // 调高温度
    AdjustUpTemperature,
    // 调低温度
    AdjustDownTemperature,
    // 设置风速
    SetWindSpeed,
    // 调大风速
    AdjustUpWindSpeed,
    // 调小风速
    AdjustDownWindSpeed,
    // 模式的切换
    SetMode,
    // 设置颜色
    SetColor,
    // 打开XX功能
    OpenFunction,
    // 关闭XX功能
    CloseFunction,
    // 取消
    Cancel,
    // 取消模式(退出模式)
    CancelMode,
    // 开启摆风
    OpenSwing,
    // 关闭摆风
    CloseSwing,

    // 2020/6/3新增
    // 角度取值30,60(默认),90
    // 开启上下X度摆动
    OpenUpAndDownSwing,
    // 开启向上X度摆动
    OpenUpSwing,
    // 开启向下X度摆动
    OpenDownSwing,
    // 开启左右X度摆动
    OpenLeftAndRightSwing,
    // 开启向左X度摆动
    OpenLeftSwing,
    // 开启向右X度摆动
    OpenRightSwing,
    // 开启前后X度摆动
    OpenForwardAndBackSwing,
    // 开启向前X度摆动
    OpenForwardSwing,
    // 开启向后X度摆动
    OpenBackSwing

}
