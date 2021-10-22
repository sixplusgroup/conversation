

package com.gmair.shop.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gmair.shop.bean.app.dto.UserCollectionDto;
import com.gmair.shop.bean.model.UserCollection;
import org.apache.commons.lang3.StringUtils;

/**
 * 用户收藏表
 *
 *
 * @date 2019-04-19 16:57:20
 */
public interface UserCollectionMapper extends BaseMapper<UserCollection> {
   IPage<UserCollectionDto> getUserCollectionDtoPageByUserId(Page page, String userId);

}
