package com.gmair.shop.bean.pay;

import lombok.Data;

/**
 * @Author Joby
 */
@Data
public class PayFeignParam {
    private String orderId;
    private String openid;
    private int price;
    private String body;
    private String ip;
}
