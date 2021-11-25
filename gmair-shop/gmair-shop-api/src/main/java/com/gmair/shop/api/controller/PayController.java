

package com.gmair.shop.api.controller;


import com.github.binarywang.wxpay.service.WxPayService;
import com.gmair.shop.api.config.ApiConfig;
import com.gmair.shop.bean.app.param.PayParam;
import com.gmair.shop.bean.model.Order;
import com.gmair.shop.bean.model.User;
import com.gmair.shop.bean.pay.PayFeignParam;
import com.gmair.shop.common.exception.GmairShopGlobalException;
import com.gmair.shop.security.util.SecurityUtils;
import com.gmair.shop.service.OrderService;
import com.gmair.shop.service.PayService;
import com.gmair.shop.service.UserService;
import com.gmair.shop.service.feign.MembershipFeignService;
import com.gmair.shop.service.feign.PayFeignService;
import finley.gmair.param.membership.IntegralWithdrawParam;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResponseData;
import finley.gmair.util.ResultData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/shop/consumer/order")
@Api(tags = "订单接口")
@AllArgsConstructor
public class PayController {

    private final PayService payService;

    private final ApiConfig apiConfig;

    private final WxPayService wxMiniPayService;

    private final PayFeignService payFeignService;

    private final OrderService orderService;

    private final UserService userService;

    private final MembershipFeignService membershipFeignService;

    /**
     * 支付接口
     */
    @PostMapping("/pay")
    @ApiOperation(value = "根据订单号进行支付", notes = "根据订单号进行支付")
    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    public ResponseData<Map<String, String>> pay(@RequestBody PayParam payParam) {

        String userId = SecurityUtils.getUser().getUserId();
        String openId = SecurityUtils.getUser().getBizUserId();
        User user = userService.getUserByUserId(userId);
        final Order order = orderService.getOrderByOrderNumber(payParam.getOrderNumbers());
        if(order.getIsPayed().equals(1)){
            throw new GmairShopGlobalException("订单已支付!");
        }
        if(order.getIsNeedIntegralOfAll()){
            ResponseData<Integer> responseData = membershipFeignService.getMembershipIntegral(user.getConsumerId());
            if(responseData.getResponseCode()!=ResponseCode.RESPONSE_OK){
                return ResponseData.error(responseData.getDescription());
            }
            if(responseData.getData()<order.getTotalIntegral()){
                return ResponseData.error("您的积分不足!");
            }
        }
        Map<String,String> responseResult = new HashMap<>();
        if(order.getIsNeedCashOfAll()){
            PayFeignParam payFeignParam = payService.getCashPayParam(userId,openId,order);
            ResultData result = payFeignService.payAllowExist(payFeignParam.getOrderId(),payFeignParam.getOpenid(),payFeignParam.getPrice(),payFeignParam.getBody(),payFeignParam.getIp());
            if(result.getResponseCode()!= ResponseCode.RESPONSE_OK){
                throw new GmairShopGlobalException("支付失败");
            }
            responseResult = (Map<String,String>)result.getData();
        }

        return ResponseData.ok(responseResult);
    }

    @PostMapping("/integral/pay")
    @ApiOperation(value = "只有积分支付时调用", notes = "只有积分支付时调用")
    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    public ResponseData<Void> integralPay(@RequestBody PayParam payParam) {
        String userId = SecurityUtils.getUser().getUserId();
        User user = userService.getUserByUserId(userId);
        final Order order = orderService.getOrderByOrderNumber(payParam.getOrderNumbers());
        if(order.getIsPayed().equals(1)){
            throw new GmairShopGlobalException("订单已支付!");
        }
        if(order.getIsNeedCashOfAll()){
            throw new GmairShopGlobalException("该订单包括现金支付");
        }
        String description = order.getProdName()+"支付";
        IntegralWithdrawParam integralWithdrawParam = new IntegralWithdrawParam(user.getConsumerId(),order.getTotalIntegral(),description);
        ResponseData<Void> responseData =  membershipFeignService.withdrawIntegral(integralWithdrawParam);
        if(responseData.getResponseCode()!=ResponseCode.RESPONSE_OK){
            return ResponseData.error(responseData.getDescription());
        }
        // 根据内部订单号更新order
        payService.paySuccess(order.getOrderNumber());

        return ResponseData.ok();
    }


    @PostMapping("/payed")
    @ApiOperation(value = "根据订单号进行更新支付状态(仅用于回调通知)", notes = "根据订单号进行更新支付状态")
    @SneakyThrows
    public ResponseData<Void> payed(String orderId){
        String userId = SecurityUtils.getUser().getUserId();
        User user = userService.getUserByUserId(userId);
        final Order order = orderService.getOrderByOrderNumber(orderId);
        if(order.getIsPayed().equals(1)){
            throw new GmairShopGlobalException("订单已支付!");
        }
        if(order.getIsNeedIntegralOfAll()){
            String description = order.getProdName()+"支付";
            IntegralWithdrawParam integralWithdrawParam = new IntegralWithdrawParam(user.getConsumerId(),order.getTotalIntegral(),description);
            ResponseData<Void> responseData =  membershipFeignService.withdrawIntegral(integralWithdrawParam);
            if(responseData.getResponseCode()!=ResponseCode.RESPONSE_OK){
                return ResponseData.error(responseData.getDescription());
            }
        }
        // 根据内部订单号更新order
        payService.paySuccess(orderId);

        return ResponseData.ok();
    }
}
