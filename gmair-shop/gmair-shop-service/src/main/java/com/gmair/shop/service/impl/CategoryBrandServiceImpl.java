

package com.gmair.shop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gmair.shop.bean.model.CategoryBrand;
import com.gmair.shop.dao.CategoryBrandMapper;
import com.gmair.shop.service.CategoryBrandService;

/**
 *
 * Created by lgh on 2018/07/13.
 */
@Service
public class CategoryBrandServiceImpl extends ServiceImpl<CategoryBrandMapper, CategoryBrand> implements CategoryBrandService {

    @Autowired
    private CategoryBrandMapper categoryBrandMapper;

}
