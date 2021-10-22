

package com.gmair.shop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gmair.shop.bean.model.PickAddr;
import com.gmair.shop.dao.PickAddrMapper;
import com.gmair.shop.service.PickAddrService;

/**
 *
 *
 */
@Service
public class PickAddrServiceImpl extends ServiceImpl<PickAddrMapper, PickAddr> implements PickAddrService {

    @Autowired
    private PickAddrMapper pickAddrMapper;

	@Override
	public void deleteByIds(Long[] ids) {
		pickAddrMapper.deleteByIds(ids);
	}


}
