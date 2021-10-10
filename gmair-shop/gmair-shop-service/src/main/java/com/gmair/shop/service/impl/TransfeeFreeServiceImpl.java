

package com.gmair.shop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gmair.shop.bean.model.TransfeeFree;
import com.gmair.shop.dao.TransfeeFreeMapper;

import com.gmair.shop.service.TransfeeFreeService;

/**
 *
 *
 */
@Service
public class TransfeeFreeServiceImpl extends ServiceImpl<TransfeeFreeMapper, TransfeeFree> implements TransfeeFreeService {

    @Autowired
    private TransfeeFreeMapper transfeeFreeMapper;

}
