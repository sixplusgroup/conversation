package finley.gmair.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.netflix.discovery.converters.Auto;
import finley.gmair.exception.MembershipGlobalException;
import finley.gmair.dao.MembershipMapper;
import finley.gmair.enums.membership.MembershipType;
import finley.gmair.model.membership.IntegralRecord;
import finley.gmair.model.membership.MembershipUser;
import finley.gmair.service.IntegralRecordService;
import finley.gmair.service.MembershipConfigService;
import finley.gmair.service.MembershipService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Author Joby
 */
@Service
public class MembershipServiceImpl extends ServiceImpl<MembershipMapper, MembershipUser> implements MembershipService {


    private final MembershipMapper membershipMapper;

    private final MembershipConfigService membershipConfigService;
    //Use constructor injection
    public MembershipServiceImpl(MembershipMapper membershipMapper, MembershipConfigService membershipConfigService) {
        this.membershipMapper = membershipMapper;
        this.membershipConfigService = membershipConfigService;
    }

    //Prevent service cycle dependency
    @Lazy
    @Autowired
    private IntegralRecordService integralRecordService;

    @Override
    public Boolean createMembership(String consumerId) {
        MembershipUser membership = new MembershipUser();

        membership.setMembershipType(MembershipType.ORDINARY.value());
        membership.setConsumerId(consumerId);
        membership.setCreateTime(new Date());

        Integer initIntegral = membershipConfigService.getConfigSignUpIntegral();
        membership.setIntegral(initIntegral);

        Boolean resultFlag = membershipMapper.insert(membership)==1;

        // log the integral operation
        IntegralRecord integralRecord = new IntegralRecord();
        integralRecord.setIsAdd(true);
        integralRecord.setDescription("加入会员");
        integralRecord.setIntegralValue(initIntegral);
        integralRecord.setMembershipUserId(membership.getId());
        integralRecordService.createRecord(integralRecord);


        return resultFlag;
    }

    @Override
    public MembershipUser getMembershipById(Long membershipId) {
        return membershipMapper.selectById(membershipId);
    }

    @Override
    public Long getMembershipIdByConsumerId(String consumerId) {
        MembershipUser membershipUser =membershipMapper.selectOne(new LambdaQueryWrapper<MembershipUser>().eq(MembershipUser::getConsumerId, consumerId));
        if(membershipUser==null){
            throw new MembershipGlobalException("this consumer isn't a membership!");
        }
        return membershipUser.getId();
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
            throw new MembershipGlobalException("您的积分不足!");
        }
        membershipUser.setIntegral(membershipUser.getIntegral()-integral);
        membershipMapper.updateById(membershipUser);
    }

    @Override
    public void updateMembership(MembershipUser membershipUser) {
        membershipMapper.updateById(membershipUser);
    }

    @Override
    public void deleteMembershipById(String id) {
        if(StrUtil.isBlank(id)){
            throw new MembershipGlobalException("param can not be blank!");
        }
        membershipMapper.deleteById(id);
    }
}
