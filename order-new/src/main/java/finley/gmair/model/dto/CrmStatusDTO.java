package finley.gmair.model.dto;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author zm
 * @date 2020/10/26 0026 11:41
 * @description 更新crm系统订单状态的DTO类
 **/
@Data
public class CrmStatusDTO {

    /**
     * 订单号（订单号+联系方式，确认唯一性）
     */
    String ddh;
    /**
     * 联系方式（订单号+联系方式，确认唯一性）
     */
    String lxfs;
    /**
     * 订单状态（默认是1，未处理）
     */
    String orderStatus;

    public CrmStatusDTO() {
        this.orderStatus = "1";
    }
}
