package com.gmair.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gmair.shop.bean.model.MembershipUser;

/**
 * @Author Joby
 */
public interface MembershipService extends IService<MembershipUser> {

    Boolean createMembership(String userId);

    MembershipUser getMembershipById(Long membershipId);

    MembershipUser getMembershipByUserId(String userId);

    Long getMembershipIdByUserId(String userId);

    void addIntegral(Long membershipId, Integer integralValue);

    void withdrawIntegralById(Long membershipId, Integer integral);


}
