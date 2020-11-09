package finley.gmair.dto;

import finley.gmair.model.machine.MachineFilterType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author: Bright Chan
 * @date: 2020/11/7 16:28
 * @description: MachineFilterInfo，用于返回数据给前端
 * Deprecated for now
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MachineFilterInfo {

    /**
     * 设备二维码
     */
    private String qrcode;

    /**
     * 设备滤网型号
     */
    private MachineFilterType machineFilterType;

    /**
     * 设备滤网是否需要清洗/更换
     * 初效滤网：0：不需要清洗；1：需要清洗
     * 高效滤网：0：无需更换；1：需要更换；2：急需更换。
     * @see finley.gmair.model.machine.EfficientFilterStatus
     */
    private int needRenewStatus;

    /**
     * 滤网提醒功能是否开启
     */
    private boolean remindSwitch;

    /**
     * 当前滤网使用周期内，提醒发送的状态
     * 初效滤网：0：没有提醒；1：提醒过
     * 高效滤网：0：没有提醒；1：提醒过一次；2：提醒过两次。
     * @see finley.gmair.model.machine.EfficientFilterRemindStatus
     */
    private int remindStatus;

    /**
     * 用户上次确认清洗/更换设备滤网的时间
     * 该字段为null的几个原因：
     *      该用户没有手动确认过；
     *      有一部分高效滤网不记录最后确认时间
     */
    private Date lastConfirmTime;

}
