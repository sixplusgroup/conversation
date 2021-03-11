package finley.gmair.dto;

import finley.gmair.model.machine.EfficientFilterRemindStatus;
import finley.gmair.model.machine.EfficientFilterStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author: Bright Chan
 * @date: 2020/11/9 11:02
 * @description: MachineEfficientFilterInfo
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MachineEfficientFilterInfo {

    /**
     * 设备二维码
     */
    private String qrcode;

    /**
     * 设备高效滤网使用状态
     * NO_NEED - 无需更换
     * NEED - 需更换
     * URGENT_NEED - 急需更换
     */
    private EfficientFilterStatus replaceStatus;

    /**
     * 滤网提醒功能是否开启
     */
    private boolean remindSwitchOn;

    /**
     * 当前滤网使用周期内，提醒发送的状态
     * REMIND_ZERO - 一次没有提醒
     * REMIND_ONCE - 提醒过一次
     * REMIND_TWICE - 提醒过两次
     */
    private EfficientFilterRemindStatus isRemindedStatus;

    /**
     * 用户上次确认清洗/更换设备滤网的时间
     * 该字段为null的几个原因：
     *      该用户没有手动确认过；
     *      有一部分高效滤网不记录最后确认时间
     */
    private Date lastConfirmTime;
}
