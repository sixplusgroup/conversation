

package com.gmair.shop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.gmair.shop.bean.model.IndexImg;
import com.gmair.shop.dao.IndexImgMapper;
import com.gmair.shop.service.IndexImgService;

import java.util.List;

/**
 *
 *
 */
@Service
public class IndexImgServiceImpl extends ServiceImpl<IndexImgMapper, IndexImg> implements IndexImgService {

    @Autowired
    private IndexImgMapper indexImgMapper;

	@Override
	public void deleteIndexImgsByIds(Long[] ids) {
		indexImgMapper.deleteIndexImgsByIds(ids);
	}

    @Override
    @Cacheable(cacheNames = "indexImg", key = "'indexImg'")
    public List<IndexImg> listIndexImgs() {
        return indexImgMapper.listIndexImgs();
    }

    @Override
    @CacheEvict(cacheNames = "indexImg", key = "'indexImg'")
    public void removeIndexImgCache() {
    }
}
