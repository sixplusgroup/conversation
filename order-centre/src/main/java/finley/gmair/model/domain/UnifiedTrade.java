package finley.gmair.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @author ：tsl
 * @date ：Created in 2021-12-18 21:50
 * @description ：统一主订单领域对象
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UnifiedTrade {
    private String tradeId;

    private String tid;

    private Integer tradePlatform;

    private String shopId;

    private int status;

    private Date createTime;

    private Date updateTime;

    private Date payTime;

    private Date endTime;

    private String consigneeName;

    private String consigneePhone;

    private String consigneeProvince;

    private String consigneeCity;

    private String consigneeDistrict;

    private String consigneeAddress;

    private Double price;

    private Double payment;

    private Double postFee;

    private String buyerMessage;

    private String sellerMemo;

    private Boolean isFuzzy;

    private Integer crmPushStatus;

    private Integer driftPushStatus;

    private List<UnifiedOrder> orderList;
}
