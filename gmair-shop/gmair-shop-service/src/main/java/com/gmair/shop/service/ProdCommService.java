

package com.gmair.shop.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gmair.shop.bean.app.dto.ProdCommDataDto;
import com.gmair.shop.bean.app.dto.ProdCommDto;
import com.gmair.shop.bean.model.ProdComm;

import java.util.List;


/**
 * 商品评论
 *
 *
 * @date 2019-04-19 10:43:57
 */
public interface ProdCommService extends IService<ProdComm> {
    ProdCommDataDto getProdCommDataByProdId(Long prodId);

    IPage<ProdCommDto> getProdCommDtoPageByUserId(Page page,String userId);

    IPage<ProdCommDto> getProdCommDtoPageByProdId(Page page, Long prodId, Integer evaluate);

    IPage<ProdComm> getProdCommPage(Page page,ProdComm prodComm);

}