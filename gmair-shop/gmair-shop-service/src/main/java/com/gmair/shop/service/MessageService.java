

package com.gmair.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gmair.shop.bean.model.Message;

/**
 *
 *
 */
public interface MessageService extends IService<Message> {

	void deleteByIds(Long[] ids);

}
