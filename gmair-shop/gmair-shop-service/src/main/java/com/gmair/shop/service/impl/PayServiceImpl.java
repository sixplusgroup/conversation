

package com.gmair.shop.service.impl;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gmair.shop.bean.enums.PayType;
import com.gmair.shop.bean.event.PaySuccessOrderEvent;
import com.gmair.shop.bean.model.Order;
import com.gmair.shop.bean.model.OrderSettlement;
import com.gmair.shop.bean.app.param.PayParam;
import com.gmair.shop.bean.pay.PayFeignParam;
import com.gmair.shop.bean.pay.PayInfoDto;
import com.gmair.shop.common.exception.GmairShopGlobalException;
import com.gmair.shop.common.util.Arith;
import com.gmair.shop.common.util.IPHelper;
import com.gmair.shop.dao.OrderMapper;
import com.gmair.shop.dao.OrderSettlementMapper;
import com.gmair.shop.service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 */
@Service
public class PayServiceImpl implements PayService {

    @Autowired
    private OrderMapper orderMapper;



    @Autowired
    private OrderSettlementMapper orderSettlementMapper;


    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private Snowflake snowflake;

    /**
     * 不同的订单号，同一个支付流水号
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public PayFeignParam pay(String userId, PayParam payParam) {

        PayFeignParam payFeignParam = new PayFeignParam();

        // 支付单号
        String orderNumber = payParam.getOrderNumbers();
        payFeignParam.setOrderId(orderNumber);
        Order order = orderMapper.getOrderByOrderNumber(orderNumber);
        payFeignParam.setBody(order.getProdName());
        payFeignParam.setPrice((int) Arith.mul(order.getActualTotal(), 100));
        payFeignParam.setIp(IPHelper.getIpAddr());
        return payFeignParam;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<String> paySuccess(String orderId) {

        String[] orderNumbersArray =orderId.split(StrUtil.COMMA);
        List<String> orderNumbers = Arrays.asList(orderNumbersArray);

        List<Order> orders = orderNumbers.stream().map(orderNumber -> orderMapper.getOrderByOrderNumber(orderNumber)).collect(Collectors.toList());
        for(Order order:orders){
            if(order.getIsPayed()==1){ // 1代表已经支付
                throw new GmairShopGlobalException("订单已支付");
            }
            if(order.getDeleteStatus()!=0){ // 0代表未删除
                throw new GmairShopGlobalException("订单无效");
            }
            if(order.getStatus()!=1){ // 1:待付款 2:待发货 3:待收货 4:待评价 5:成功 6:失败
                throw new GmairShopGlobalException("订单无效");
            }
        }
        // 将订单改为已支付状态
        orderMapper.updateByToPaySuccess(orderNumbers, PayType.WECHATPAY.value());

        eventPublisher.publishEvent(new PaySuccessOrderEvent(orders));
        return orderNumbers;
    }

}
