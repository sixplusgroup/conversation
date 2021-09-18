

package com.gmair.shop.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gmair.shop.bean.app.dto.MyOrderItemDto;
import com.gmair.shop.bean.app.dto.ProductDto;
import com.gmair.shop.common.util.PageAdapter;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gmair.shop.bean.app.dto.MyOrderDto;
import com.gmair.shop.bean.model.Order;
import com.gmair.shop.dao.OrderMapper;
import com.gmair.shop.service.MyOrderService;

/**
 *
 */
@Service
public class MyOrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements MyOrderService {

    @Autowired
    private OrderMapper orderMapper;

    private static final Logger log = LoggerFactory.getLogger(MyOrderServiceImpl.class);


    @Override
    public IPage<MyOrderDto> pageMyOrderByUserIdAndStatus(Page<MyOrderDto> page, String userId, Integer status) {
        page.setRecords(orderMapper.listMyOrderByUserIdAndStatus(new PageAdapter(page), userId, status));
        page.setTotal(orderMapper.countMyOrderByUserIdAndStatus(userId, status));
        return page;
    }

}
