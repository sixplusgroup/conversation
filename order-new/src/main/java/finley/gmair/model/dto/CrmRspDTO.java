package finley.gmair.model.dto;

import lombok.Data;

/**
 * @author zm
 * @date 2020/10/26 0026 19:44
 * @description 调用CRM的api的返回对象
 **/
@Data
public class CrmRspDTO {

    private String ResponseCode;

    private String Description;
}
