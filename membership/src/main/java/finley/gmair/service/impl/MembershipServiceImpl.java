package finley.gmair.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import finley.gmair.exception.MembershipGlobalException;
import finley.gmair.dao.MembershipMapper;
import finley.gmair.enums.membership.MembershipType;
import finley.gmair.model.membership.MembershipUser;
import finley.gmair.service.MembershipService;
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
    public Boolean createMembership(String consumerId) {
        MembershipUser membership = new MembershipUser();
        membership.setIntegral(0);
        membership.setMembershipType(MembershipType.ORDINARY.value());
        membership.setConsumerId(consumerId);
        membership.setCreateTime(new Date());
        return membershipMapper.insert(membership)==1;
    }

    @Override
    public MembershipUser getMembershipById(Long membershipId) {
        return membershipMapper.selectById(membershipId);
    }

    @Override
    public Long getMembershipIdByConsumerId(String consumerId) {
        Long membershipId =membershipMapper.selectOne(new LambdaQueryWrapper<MembershipUser>().eq(MembershipUser::getConsumerId, consumerId)).getId();
        if(membershipId==null||membershipId==0){
            throw new MembershipGlobalException("this consumer isn't a membership!");
        }
        return membershipId;
    }

    @Override
    public MembershipUser getMembershipByConsumerId(String consumerId) {
        MembershipUser membershipUser = membershipMapper.selectOne(new LambdaQueryWrapper<MembershipUser>().eq(MembershipUser::getConsumerId, consumerId));
        if(membershipUser==null){
            throw new MembershipGlobalException("this consumer isn't a membership!");
        }
        return membershipUser;
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
            throw new MembershipGlobalException("can not find the membership!");
        }
        if(integral==null||integral<0){
            throw new MembershipGlobalException("the parameters are invalid!");
        }
        if(membershipUser.getIntegral()<integral){
            throw new MembershipGlobalException("the membership don't have enough integral!");
        }
        membershipUser.setIntegral(membershipUser.getIntegral()-integral);
        membershipMapper.updateById(membershipUser);
    }
}
