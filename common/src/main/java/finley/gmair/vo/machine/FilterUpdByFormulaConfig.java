package finley.gmair.vo.machine;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: Bright Chan
 * @date: 2020/11/27 21:02
 * @description: 具有高效滤网且是通过公式来更新滤网状态的设备的滤网提醒配置
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilterUpdByFormulaConfig {

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
     * 无t_run或t_run = 0时，此变量值为false
     * 否则为true
     * 此变量的取值直接决定了下面的所有参数的取值，
     * 即下面的所有参数都有两种取值
     */
    private boolean tRun;

    /**
     * 设备滤网总使用寿命
     */
    private int totalTime;

    /**
     * 公式参数一
     */
    private double paramOne;

    /**
     * 公式参数二
     */
    private double paramTwo;

    /**
     * 发送第一次提醒的阈值，即公式大于等于此值就会发送第一次提醒
     */
    private double firstRemindThreshold;

    /**
     * 发送第二次提醒的阈值，即公式大于等于此值就会发送第二次提醒
     */
    private double secondRemindThreshold;

}
