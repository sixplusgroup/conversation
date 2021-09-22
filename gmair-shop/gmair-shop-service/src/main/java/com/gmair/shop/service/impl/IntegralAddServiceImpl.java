package com.gmair.shop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gmair.shop.bean.model.IntegralAdd;
import com.gmair.shop.common.exception.GmairShopGlobalException;
import com.gmair.shop.dao.IntegralAddMapper;
import com.gmair.shop.dao.MembershipMapper;
import com.gmair.shop.service.IntegralAddService;
import com.gmair.shop.service.MembershipService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Author Joby
 */
@Service
@AllArgsConstructor
public class IntegralAddServiceImpl extends ServiceImpl<IntegralAddMapper, IntegralAdd> implements IntegralAddService {

    private final IntegralAddMapper integralAddMapper;

    private final MembershipService membershipService;

    @Override
    public void createAdd(IntegralAdd integralAdd) {

        if(membershipService.getMembershipById(integralAdd.getMembershipUserId())==null){
            throw new GmairShopGlobalException("can not find the membership!");
        }

        integralAddMapper.insert(integralAdd);
    }

    @Override
    public void confirmIntegralById(Long integralAddId) {
        IntegralAdd integralAdd = integralAddMapper.selectById(integralAddId);
        if(integralAdd.isConfirmed()){
            throw new GmairShopGlobalException("this record is already confirmed!");
        }
        integralAdd.setConfirmed(true);
        integralAdd.setConfirmedTime(new Date());
        integralAddMapper.updateById(integralAdd);
        membershipService.addIntegral(integralAdd.getMembershipUserId(),integralAdd.getIntegralValue());
    }
}
