

package com.gmair.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gmair.shop.bean.model.IndexImg;

import java.util.List;

/**
 *
 *
 */
public interface IndexImgService extends IService<IndexImg> {

	void deleteIndexImgsByIds(Long[] ids);

    List<IndexImg> listIndexImgs();

    void removeIndexImgCache();
}
