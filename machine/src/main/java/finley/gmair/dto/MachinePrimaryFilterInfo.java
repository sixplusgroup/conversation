package finley.gmair.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author: Bright Chan
 * @date: 2020/11/9 11:02
 * @description: MachinePrimaryFilterInfo
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MachinePrimaryFilterInfo {

    /**
     * 设备二维码
     */
    private String qrcode;

    /**
     * 设备是否需要清洗
     */
    private boolean needClean;

    /**
     * 滤网提醒功能是否开启
     */
    private boolean remindSwitchOn;

    /**
     * 当前滤网使用周期内，提醒发送的状态
     * 初效滤网：false：没有提醒；true：提醒过
     */
    private boolean isReminded;

    /**
     * 用户上次确认清洗/更换设备滤网的时间
     * 该字段为null的原因：
     *      该用户没有手动确认过
     */
    private Date lastConfirmTime;
}
