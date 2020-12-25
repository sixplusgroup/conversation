package finley.gmair.vo.machine;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: Bright Chan
 * @date: 2020/11/27 21:00
 * @description: 具有高效滤网且是通过MQTT获取Remain来更新滤网状态的设备的滤网提醒配置
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilterUpdByMQTTConfig {

    /**
     * 设备类型编号
     */
    private String modelId;

    /**
     * 设备类型名
     */
    private String modelName;

    /**
     * 设备类型code，二级分类
     */
    private String modelCode;

    /**
     * 发送第一次提醒的阈值，单位小时
     */
    private int firstRemind;

    /**
     * 发送第二次提醒的阈值，单位小时
     */
    private int secondRemind;

    /**
     * 设备滤网总使用寿命，单位小时
     */
    private int resetHour;
}
