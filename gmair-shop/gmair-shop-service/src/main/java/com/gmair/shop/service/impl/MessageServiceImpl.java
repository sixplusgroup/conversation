

package com.gmair.shop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gmair.shop.bean.model.Message;
import com.gmair.shop.dao.MessageMapper;
import com.gmair.shop.service.MessageService;

/**
 *
 *
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    @Autowired
    private MessageMapper messageMapper;

	@Override
	public void deleteByIds(Long[] ids) {
		messageMapper.deleteByIds(ids);
	}

}