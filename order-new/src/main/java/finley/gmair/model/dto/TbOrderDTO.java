package finley.gmair.model.dto;

import com.taobao.api.domain.Order;
import finley.gmair.util.IDGenerator;
import lombok.Data;

/**
 * @author zm
 * @date 2020/10/21 0021 12:34
 * @description 淘宝Order的包装类，用于转存到中台系统order表中
 **/
@Data
public class TbOrderDTO {

    private String orderId;
    private Order tbOrder;

    public TbOrderDTO() {
    }

    public TbOrderDTO(Order tbOrder) {
        this.tbOrder = tbOrder;
        this.orderId = IDGenerator.generate("CentreOrder");
    }
}
