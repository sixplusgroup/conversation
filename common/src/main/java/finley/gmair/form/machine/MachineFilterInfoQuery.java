package finley.gmair.form.machine;

import finley.gmair.model.machine.MachineFilterType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: Bright Chan
 * @date: 2020/11/8 12:57
 * @description: 前端传回来的查询设备滤网状况的查询条件
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MachineFilterInfoQuery {

    /**
     * 当前页，从1开始，必填
     */
    private int pageIndex;

    /**
     * 页大小，必填
     */
    private int pageSize;

    /**
     * 指定设备的二维码，选填
     */
    private String qrcode;

    /**
     * 设备滤网类型，必填
     */
    private MachineFilterType machineFilterType;

    /**
     * 设备型号名，对应数据库表goods_model中的model_name字段，选填
     */
    private String machineModelName;

    /**
     * 设备型号code，对应数据库表goods_model中的model_code字段，选填
     */
    private String machineModelCode;
}
