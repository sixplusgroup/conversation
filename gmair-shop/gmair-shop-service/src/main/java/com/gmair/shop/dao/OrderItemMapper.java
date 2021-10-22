

package com.gmair.shop.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.gmair.shop.bean.app.dto.OrderItemDto;
import com.gmair.shop.bean.model.OrderItem;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface OrderItemMapper extends BaseMapper<OrderItem> {

	List<OrderItem> listByOrderNumber(@Param("orderNumber") String orderNumber);
	
	void insertBatch(List<OrderItem> orderItems);
	
//	List<OrderItem> getPayByOrderNumber(@Param("orderNumber") String orderNumber);

}