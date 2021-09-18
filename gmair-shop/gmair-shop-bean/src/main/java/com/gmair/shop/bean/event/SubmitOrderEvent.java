

package com.gmair.shop.bean.event;

import com.gmair.shop.bean.app.dto.ShopCartItemDto;
import com.gmair.shop.bean.app.dto.ShopCartOrderDto;
import com.gmair.shop.bean.app.dto.ShopCartOrderMergerDto;
import com.gmair.shop.bean.app.param.OrderParam;
import com.gmair.shop.bean.model.Order;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * 提交订单时的事件
 *
 */
@Data
@AllArgsConstructor
public class SubmitOrderEvent {
    /**
     * 完整的订单信息
     */
    private final ShopCartOrderMergerDto mergerOrder;

    private List<Order> orders;

}
