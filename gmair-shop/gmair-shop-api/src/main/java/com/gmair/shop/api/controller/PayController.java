

package com.gmair.shop.api.controller;

import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.service.WxPayService;
import com.gmair.shop.api.config.ApiConfig;
import com.gmair.shop.bean.app.param.PayParam;
import com.gmair.shop.bean.pay.PayFeignParam;
import com.gmair.shop.bean.pay.PayInfoDto;
import com.gmair.shop.common.exception.GmairShopGlobalException;
import com.gmair.shop.common.util.Arith;
import com.gmair.shop.common.util.IPHelper;
import com.gmair.shop.security.entity.GmairUser;
import com.gmair.shop.security.util.SecurityUtils;
import com.gmair.shop.service.PayService;
import com.gmair.shop.service.feign.PayFeignService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/p/order")
@Api(tags = "订单接口")
@AllArgsConstructor
public class PayController {

    private final PayService payService;

    private final ApiConfig apiConfig;

    private final WxPayService wxMiniPayService;

    private final PayFeignService payFeignService;

    /**
     * 支付接口
     */
    @PostMapping("/pay")
    @ApiOperation(value = "根据订单号进行支付", notes = "根据订单号进行支付")
    @SneakyThrows
    public ResponseEntity<Map<String, String>> pay(@RequestBody PayParam payParam) {

        GmairUser user = SecurityUtils.getUser();
        String userId = user.getUserId();
        String openId = user.getBizUserId();

        PayFeignParam payFeignParam = payService.pay(userId, payParam);
        payFeignParam.setOpenid(openId);
        ResultData result = payFeignService.createTrade(payFeignParam.getOrderId(),payFeignParam.getOpenid(),payFeignParam.getPrice(),payFeignParam.getBody(),payFeignParam.getIp());
        if(result.getResponseCode()!= ResponseCode.RESPONSE_OK){
            throw new GmairShopGlobalException("支付失败");
        }
        return ResponseEntity.ok((Map<String,String>)result.getData());
    }

    @PostMapping("/payed")
    @ApiOperation(value = "根据订单号进行更新支付状态", notes = "根据订单号进行更新支付状态")
    @SneakyThrows
    public ResponseEntity<Void> payed(String orderId){

        // 根据内部订单号更新order
        payService.paySuccess(orderId);

        return ResponseEntity.ok().build();
    }
}
