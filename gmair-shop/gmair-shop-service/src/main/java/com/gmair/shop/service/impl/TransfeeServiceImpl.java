

package com.gmair.shop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gmair.shop.bean.model.Transfee;
import com.gmair.shop.dao.TransfeeMapper;
import com.gmair.shop.service.TransfeeService;

/**
 *
 *
 */
@Service
public class TransfeeServiceImpl extends ServiceImpl<TransfeeMapper, Transfee> implements TransfeeService {

    @Autowired
    private TransfeeMapper transfeeMapper;

}
