package finley.gmair.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author ：tsl
 * @date ：Created in 2021-12-18 21:51
 * @description ：
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UnifiedOrder {
    private String orderId;

    private String tradeId;

    private String tid;

    private String oid;

    private Integer status;

    private String numId;

    private String skuId;

    private Integer num;

    private Double price;

    private Double payment;

    private String logisticsCompany;

    private String logisticsId;
}
