

package com.gmair.shop.dao;

import org.apache.ibatis.annotations.Param;

import com.gmair.shop.bean.model.PickAddr;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface PickAddrMapper extends BaseMapper<PickAddr> {

	void deleteByIds(@Param("ids") Long[] ids);
}