package com.gmair.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gmair.shop.bean.model.IntegralAdd;

/**
 * @Author Joby
 */
public interface IntegralAddService extends IService<IntegralAdd> {

    void createAdd(IntegralAdd integralAdd);

    void confirmIntegralById(Long integralAddId);
}
