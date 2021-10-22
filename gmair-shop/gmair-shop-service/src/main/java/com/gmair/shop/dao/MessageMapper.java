

package com.gmair.shop.dao;

import com.gmair.shop.bean.model.Message;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface MessageMapper extends BaseMapper<Message> {

	void deleteByIds(Long[] ids);
}