

package com.gmair.shop.service;

import com.gmair.shop.bean.app.param.PayParam;
import com.gmair.shop.bean.pay.PayInfoDto;

import java.util.List;

/**
 *
 */
public interface PayService {


    PayInfoDto pay(String userId, PayParam payParam);

    List<String> paySuccess(String payNo, String bizPayNo);

}
