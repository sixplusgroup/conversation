

package com.gmair.shop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gmair.shop.dao.UserAddrOrderMapper;
import org.springframework.stereotype.Service;

import com.gmair.shop.bean.model.UserAddrOrder;
import com.gmair.shop.service.UserAddrOrderService;

@Service
public class UserAddrOrderServiceImpl extends ServiceImpl<UserAddrOrderMapper, UserAddrOrder> implements UserAddrOrderService{

}
