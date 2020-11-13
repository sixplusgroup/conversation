package finley.gmair.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: Bright Chan
 * @date: 2020/11/11 9:35
 * @description: MachineTypeInfo，设备型号信息
 * Deprecated for now
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MachineTypeInfo {

    /**
     * 设备型号code
     */
    private String modelCode;

    /**
     * 设备型号名
     */
    private String modelName;

}
