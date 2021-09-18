

package com.gmair.shop.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gmair.shop.bean.model.OrderItem;

/**
 *
 *
 */
public interface OrderItemService extends IService<OrderItem> {

	List<OrderItem> getOrderItemsByOrderNumber(String orderNumber);

}
