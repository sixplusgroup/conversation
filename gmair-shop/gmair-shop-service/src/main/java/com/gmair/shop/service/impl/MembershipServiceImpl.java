package com.gmair.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gmair.shop.bean.enums.MembershipType;
import com.gmair.shop.bean.model.MembershipUser;
import com.gmair.shop.bean.model.UserCollection;
import com.gmair.shop.common.exception.GmairShopGlobalException;
import com.gmair.shop.dao.MembershipMapper;
import com.gmair.shop.service.MembershipService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Author Joby
 */
@AllArgsConstructor
@Service
public class MembershipServiceImpl extends ServiceImpl<MembershipMapper, MembershipUser> implements MembershipService {

    private final MembershipMapper membershipMapper;

    @Override
    public Boolean createMembership(String userId) {
        MembershipUser membership = new MembershipUser();
        membership.setIntegral(0);
        membership.setMembershipType(MembershipType.ORDINARY.value());
        membership.setUserId(userId);
        membership.setCreateTime(new Date());
        return membershipMapper.insert(membership)==1;
    }

    @Override
    public MembershipUser getMembershipById(Long membershipId) {
        return membershipMapper.selectById(membershipId);
    }

    @Override
    public Long getMembershipIdByUserId(String userId) {
        return membershipMapper.selectOne(new LambdaQueryWrapper<MembershipUser>().eq(MembershipUser::getUserId, userId)).getId();
    }

    @Override
    public MembershipUser getMembershipByUserId(String userId) {
        return membershipMapper.selectOne(new LambdaQueryWrapper<MembershipUser>().eq(MembershipUser::getUserId, userId));
    }

    @Override
    public void addIntegral(Long membershipId, Integer integralValue) {
        MembershipUser membershipUser = membershipMapper.selectById(membershipId);
        membershipUser.setIntegral(membershipUser.getIntegral()+integralValue);
        membershipMapper.updateById(membershipUser);
    }

    @Override
    public void withdrawIntegralById(Long membershipId, Integer integral) {
        MembershipUser membershipUser =  membershipMapper.selectById(membershipId);
        if(membershipUser==null){
            throw new GmairShopGlobalException("can not find the membership!");
        }
        if(integral==null||integral<0){
            throw new GmairShopGlobalException("the parameters are invalid!");
        }
        if(membershipUser.getIntegral()<integral){
            throw new GmairShopGlobalException("the membership don't have enough integral!");
        }
        membershipUser.setIntegral(membershipUser.getIntegral()-integral);
        membershipMapper.updateById(membershipUser);
    }
}
