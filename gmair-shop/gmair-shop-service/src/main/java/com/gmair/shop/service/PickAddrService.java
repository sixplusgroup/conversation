

package com.gmair.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gmair.shop.bean.model.PickAddr;

/**
 *
 *
 */
public interface PickAddrService extends IService<PickAddr> {

	void deleteByIds(Long[] ids);

}
