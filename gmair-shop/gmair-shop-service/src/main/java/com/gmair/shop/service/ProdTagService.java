

package com.gmair.shop.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.gmair.shop.bean.model.ProdTag;

import java.util.List;

/**
 * 商品分组标签
 *
 *
 * @date 2019-04-18 10:48:44
 */
public interface ProdTagService extends IService<ProdTag> {

    List<ProdTag> listProdTag();

    void removeProdTag();
}
