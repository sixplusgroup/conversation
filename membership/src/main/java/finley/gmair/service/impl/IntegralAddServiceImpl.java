package finley.gmair.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import finley.gmair.exception.MembershipGlobalException;
import finley.gmair.dao.IntegralAddMapper;
import finley.gmair.model.membership.IntegralAdd;
import finley.gmair.service.IntegralAddService;
import finley.gmair.service.MembershipService;
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
            throw new MembershipGlobalException("can not find the membership!");
        }

        integralAddMapper.insert(integralAdd);
    }

    @Override
    public void confirmIntegralById(Long integralAddId) {
        IntegralAdd integralAdd = integralAddMapper.selectById(integralAddId);
        if(integralAdd.getIsConfirmed()){
            throw new MembershipGlobalException("this record is already confirmed!");
        }
        integralAdd.setIsConfirmed(true);
        integralAdd.setConfirmedTime(new Date());
        integralAddMapper.updateById(integralAdd);
        membershipService.addIntegral(integralAdd.getMembershipUserId(),integralAdd.getIntegralValue());
    }
}
