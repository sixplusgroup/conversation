

package com.gmair.shop.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gmair.shop.bean.app.dto.UserCollectionDto;
import com.gmair.shop.bean.model.UserCollection;

/**
 * 用户收藏表
 *
 *
 * @date 2019-04-19 16:57:20
 */
public interface UserCollectionService extends IService<UserCollection> {
    IPage<UserCollectionDto> getUserCollectionDtoPageByUserId(Page page, String userId);
}
