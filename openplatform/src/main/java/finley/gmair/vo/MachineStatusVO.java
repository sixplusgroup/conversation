package finley.gmair.vo;

import lombok.Data;

/**
 * @author lyy
 * @date 2020-07-11 1:30 下午
 */
@Data
public class MachineStatusVO {
    String qrCode;
    String msg = "";
    Object data;
}
