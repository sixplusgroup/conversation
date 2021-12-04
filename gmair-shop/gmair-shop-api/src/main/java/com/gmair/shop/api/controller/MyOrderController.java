

package com.gmair.shop.api.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gmair.shop.bean.enums.OrderStatus;
import com.gmair.shop.common.exception.GmairShopGlobalException;
import com.gmair.shop.common.util.PageParam;
import com.gmair.shop.bean.app.dto.*;
import com.gmair.shop.security.util.SecurityUtils;
import com.gmair.shop.service.*;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import com.gmair.shop.bean.model.Order;
import com.gmair.shop.bean.model.OrderItem;
import com.gmair.shop.bean.model.ShopDetail;
import com.gmair.shop.bean.model.UserAddrOrder;
import com.gmair.shop.common.util.Arith;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;

@RestController
@RequestMapping("/shop/consumer/myOrder")
@Api(tags = "我的订单接口")
@AllArgsConstructor
public class MyOrderController {

    private final OrderService orderService;

    private final MapperFacade mapperFacade;

    private final UserAddrOrderService userAddrOrderService;

    private final ProductService productService;

    private final SkuService skuService;

    private final MyOrderService myOrderService;

    private final ShopDetailService shopDetailService;

    private final OrderItemService orderItemService;


    /**
     * 订单详情信息接口
     */
    @GetMapping("/orderDetail")
    @ApiOperation(value = "订单详情信息", notes = "根据订单号获取订单详情信息")
    @ApiImplicitParam(name = "orderNumber", value = "订单号", required = true, dataType = "String")
    public ResponseEntity<OrderShopDto> orderDetail(@RequestParam(value = "orderNumber", required = true) String orderNumber) {

        String userId = SecurityUtils.getUser().getUserId();
        OrderShopDto orderShopDto = new OrderShopDto();

        Order order = orderService.getOrderByOrderNumber(orderNumber);

        if (order == null) {
            throw new RuntimeException("该订单不存在");
        }
        if (!Objects.equals(order.getUserId(), userId)) {
            throw new RuntimeException("你没有权限获取该订单信息");
        }

        ShopDetail shopDetail = shopDetailService.getShopDetailByShopId(order.getShopId());
        UserAddrOrder userAddrOrder = userAddrOrderService.getById(order.getAddrOrderId());
        UserAddrDto userAddrDto = mapperFacade.map(userAddrOrder, UserAddrDto.class);
        List<OrderItem> orderItems = orderItemService.getOrderItemsByOrderNumber(orderNumber);
        List<OrderItemDto> orderItemDtos = mapperFacade.mapAsList(orderItems, OrderItemDto.class);

        orderShopDto.setShopId(shopDetail.getShopId());
        orderShopDto.setShopName(shopDetail.getShopName());
        orderShopDto.setActualTotal(order.getActualTotal());
        orderShopDto.setUserAddrDto(userAddrDto);
        orderShopDto.setOrderItemDtos(orderItemDtos);
        orderShopDto.setTransfee(order.getFreightAmount());
        orderShopDto.setReduceAmount(order.getReduceAmount());
        orderShopDto.setCreateTime(order.getCreateTime());
        orderShopDto.setRemarks(order.getRemarks());
        orderShopDto.setStatus(order.getStatus());

        orderShopDto.setIsNeedCashOfAll(order.getIsNeedCashOfAll());
        orderShopDto.setIsNeedIntegralOfAll(order.getIsNeedIntegralOfAll());

        double total = 0.0;
        Integer totalNum = 0;
        Integer totalIntegral = 0;
        for (OrderItemDto orderItem : orderShopDto.getOrderItemDtos()) {
            total = Arith.add(total, orderItem.getProductTotalAmount());
            totalNum += orderItem.getProdCount();
            totalIntegral += orderItem.getIntegralTotalAmount();
        }
        orderShopDto.setTotal(total);
        orderShopDto.setTotalNum(totalNum);
        orderShopDto.setTotalIntegral(totalIntegral);

        return ResponseEntity.ok(orderShopDto);
    }


    /**
     * 订单列表接口
     */
    @GetMapping("/myOrder")
    @ApiOperation(value = "订单列表信息", notes = "根据订单状态获取订单列表信息，状态为0时获取所有订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "status", value = "订单状态 1:待付款 2:待发货 3:待收货 4:待评价 5:成功 6:失败", required = false, dataType = "Integer")
    })
    public ResponseEntity<IPage<MyOrderDto>> myOrder(@RequestParam(value = "status") Integer status,PageParam<MyOrderDto> page) {

        String userId = SecurityUtils.getUser().getUserId();
        IPage<MyOrderDto> myOrderDtoIpage = myOrderService.pageMyOrderByUserIdAndStatus(page, userId, status);
        return ResponseEntity.ok(myOrderDtoIpage);
    }

    /**
     * 取消订单
     */
    @PutMapping("/cancel/{orderNumber}")
    @ApiOperation(value = "根据订单号取消订单", notes = "根据订单号取消订单")
    @ApiImplicitParam(name = "orderNumber", value = "订单号", required = true, dataType = "String")
    public ResponseEntity<String> cancel(@PathVariable("orderNumber") String orderNumber) {
        String userId = SecurityUtils.getUser().getUserId();
        Order order = orderService.getOrderByOrderNumber(orderNumber);
        if (!Objects.equals(order.getUserId(), userId)) {
            throw new GmairShopGlobalException("你没有权限获取该订单信息");
        }
        if (!Objects.equals(order.getStatus(), OrderStatus.UNPAY.value())) {
            throw new GmairShopGlobalException("订单已支付，无法取消订单");
        }
        List<OrderItem> orderItems = orderItemService.getOrderItemsByOrderNumber(orderNumber);
        order.setOrderItems(orderItems);
        // 取消订单
        orderService.cancelOrders(Arrays.asList(order));

        // 清除缓存
        for (OrderItem orderItem : orderItems) {
            productService.removeProductCacheByProdId(orderItem.getProdId());
            skuService.removeSkuCacheBySkuId(orderItem.getSkuId(),orderItem.getProdId());
        }
        return ResponseEntity.ok().build();
    }


    /**
     * 确认收货
     */
    @PutMapping("/receipt/{orderNumber}")
    @ApiOperation(value = "根据订单号确认收货", notes = "根据订单号确认收货")
    public ResponseEntity<String> receipt(@PathVariable("orderNumber") String orderNumber) {
        String userId = SecurityUtils.getUser().getUserId();
        Order order = orderService.getOrderByOrderNumber(orderNumber);
        if (!Objects.equals(order.getUserId(), userId)) {
            throw new GmairShopGlobalException("你没有权限获取该订单信息");
        }
        if (!Objects.equals(order.getStatus(), OrderStatus.CONSIGNMENT.value())) {
            throw new GmairShopGlobalException("订单未发货，无法确认收货");
        }
        List<OrderItem> orderItems = orderItemService.getOrderItemsByOrderNumber(orderNumber);
        order.setOrderItems(orderItems);
        // 确认收货
        orderService.confirmOrder(Arrays.asList(order));

        for (OrderItem orderItem : orderItems) {
            productService.removeProductCacheByProdId(orderItem.getProdId());
            skuService.removeSkuCacheBySkuId(orderItem.getSkuId(),orderItem.getProdId());
        }
        return ResponseEntity.ok().build();
    }

    /**
     * 删除订单
     */
    @DeleteMapping("/{orderNumber}")
    @ApiOperation(value = "根据订单号删除订单", notes = "根据订单号删除订单")
    @ApiImplicitParam(name = "orderNumber", value = "订单号", required = true, dataType = "String")
    public ResponseEntity<String> delete(@PathVariable("orderNumber") String orderNumber) {
        String userId = SecurityUtils.getUser().getUserId();

        Order order = orderService.getOrderByOrderNumber(orderNumber);
        if (order == null) {
            throw new GmairShopGlobalException("该订单不存在");
        }
        if (!Objects.equals(order.getUserId(), userId)) {
            throw new GmairShopGlobalException("你没有权限获取该订单信息");
        }
        if (!Objects.equals(order.getStatus(), OrderStatus.SUCCESS.value()) && !Objects.equals(order.getStatus(), OrderStatus.CLOSE.value()) ) {
            throw new GmairShopGlobalException("订单未完成或未关闭，无法删除订单");
        }

        // 删除订单
        orderService.deleteOrders(Arrays.asList(order));

        return ResponseEntity.ok("删除成功");
    }

    /**
     * 获取我的订单订单数量
     */
    @GetMapping("/orderCount")
    @ApiOperation(value = "获取我的订单订单数量", notes = "获取我的订单订单数量")
    public ResponseEntity<OrderCountData> getOrderCount() {
        String userId = SecurityUtils.getUser().getUserId();
        OrderCountData orderCountMap = orderService.getOrderCount(userId);
        return ResponseEntity.ok(orderCountMap);
    }


}