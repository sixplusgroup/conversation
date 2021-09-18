

package com.gmair.shop.api.listener;

import com.google.common.collect.Lists;
import com.gmair.shop.bean.app.dto.*;
import com.gmair.shop.bean.app.param.OrderParam;
import com.gmair.shop.bean.event.ConfirmOrderEvent;
import com.gmair.shop.bean.event.ShopCartEvent;
import com.gmair.shop.bean.model.Product;
import com.gmair.shop.bean.model.Sku;
import com.gmair.shop.bean.model.UserAddr;
import com.gmair.shop.bean.order.ConfirmOrderOrder;
import com.gmair.shop.bean.order.ShopCartEventOrder;
import com.gmair.shop.common.exception.GmairShopBindException;
import com.gmair.shop.common.util.Arith;
import com.gmair.shop.security.util.SecurityUtils;
import com.gmair.shop.service.ProductService;
import com.gmair.shop.service.SkuService;
import com.gmair.shop.service.TransportManagerService;
import com.gmair.shop.service.UserAddrService;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 确认订单信息时的默认操作
 *
 */
@Component("defaultConfirmOrderListener")
@AllArgsConstructor
public class ConfirmOrderListener {

    private final UserAddrService userAddrService;

    private final TransportManagerService transportManagerService;

    private final ProductService productService;

    private final SkuService skuService;

    /**
     * 计算订单金额
     */
    @EventListener(ConfirmOrderEvent.class)
    @Order(ConfirmOrderOrder.DEFAULT)
    public void defaultConfirmOrderEvent(ConfirmOrderEvent event) {


        ShopCartOrderDto shopCartOrderDto = event.getShopCartOrderDto();

        OrderParam orderParam = event.getOrderParam();

        String userId = SecurityUtils.getUser().getUserId();

        // 订单的地址信息
        UserAddr userAddr = userAddrService.getUserAddrByUserId(orderParam.getAddrId(), userId);

        double total = 0.0;

        int totalCount = 0;

        double transfee = 0.0;

        for (ShopCartItemDto shopCartItem : event.getShopCartItems()) {
            // 获取商品信息
            Product product = productService.getProductByProdId(shopCartItem.getProdId());
            // 获取sku信息
            Sku sku = skuService.getSkuBySkuId(shopCartItem.getSkuId());
            if (product == null || sku == null) {
                throw new GmairShopBindException("购物车包含无法识别的商品");
            }
            if (product.getStatus() != 1 || sku.getStatus() != 1) {
                throw new GmairShopBindException("商品[" + sku.getProdName() + "]已下架");
            }

            totalCount = shopCartItem.getProdCount() + totalCount;
            total = Arith.add(shopCartItem.getProductTotalAmount(), total);
            // 用户地址如果为空，则表示该用户从未设置过任何地址相关信息
            if (userAddr != null) {
                // 每个产品的运费相加
                transfee = Arith.add(transfee, transportManagerService.calculateTransfee(shopCartItem, userAddr));
            }

            shopCartItem.setActualTotal(shopCartItem.getProductTotalAmount());
            shopCartOrderDto.setActualTotal(Arith.sub(total, transfee));
            shopCartOrderDto.setTotal(total);
            shopCartOrderDto.setTotalCount(totalCount);
            shopCartOrderDto.setTransfee(transfee);
        }
    }
}
