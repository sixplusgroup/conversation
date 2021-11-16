

package com.gmair.shop.service;

import com.gmair.shop.bean.app.param.PayParam;
import com.gmair.shop.bean.model.Order;
import com.gmair.shop.bean.pay.PayFeignParam;
import com.gmair.shop.bean.pay.PayInfoDto;

import java.util.List;

/**
 *
 */
public interface PayService {


    PayFeignParam getCashPayParam(String userId,String openId , Order order);

    List<String> paySuccess(String orderId);


}
