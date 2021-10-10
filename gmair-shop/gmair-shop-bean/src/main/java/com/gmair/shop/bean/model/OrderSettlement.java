

package com.gmair.shop.bean.model;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("tz_order_settlement")
public class OrderSettlement implements Serializable {
    /**
     * 支付结算单据ID
     */
    @TableId

    private Long settlementId;

    /**
     * 用户系统内部的订单号
     */
    private String payNo;

    /**
     * 外部订单流水号
     */
    private String bizPayNo;
    
    /**
     * 订单号
     */
    private String orderNumber;
    
    /**
     * 支付方式 0 手动代付 1 微信支付 2 支付宝
     */

    private Integer payType;

    /**
     * 支付金额
     */
    private Double payAmount;

    /**
     * 支付积分
     */
    private Integer payIntegralAmount;

    /**
     * 是否需要现金
     */
    private Boolean isNeedCashOfAll;

    /**
     * 是否需要积分
     */
    private Boolean isNeedIntegralOfAll;


    /**
     * 用户ID
     */

    private String userId;

    /**
     * 是否清算 0:否 1:是
     */

    private Integer isClearing;

    /**
     * 创建时间
     */

    private Date createTime;

    /**
     * 清算时间
     */

    private Date clearingTime;

    /**
     * 支付状态
     */
    private Integer payStatus;
    
    /**
     * 版本号
     */
    private Integer version;

}