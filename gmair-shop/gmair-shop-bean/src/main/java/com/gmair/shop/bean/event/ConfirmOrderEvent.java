

package com.gmair.shop.bean.event;

import com.gmair.shop.bean.app.dto.ShopCartDto;
import com.gmair.shop.bean.app.dto.ShopCartItemDto;
import com.gmair.shop.bean.app.dto.ShopCartOrderDto;
import com.gmair.shop.bean.app.dto.ShopCartOrderMergerDto;
import com.gmair.shop.bean.app.param.OrderParam;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * 确认订单时的事件
 *
 */
@Data
@AllArgsConstructor
public class ConfirmOrderEvent {

    /**
     * 购物车已经组装好的店铺订单信息
     */
    private ShopCartOrderDto shopCartOrderDto;

    /**
     * 下单请求的参数
     */
    private OrderParam orderParam;

    /**
     * 店铺中的所有商品项
     */
    private List<ShopCartItemDto> shopCartItems;
}