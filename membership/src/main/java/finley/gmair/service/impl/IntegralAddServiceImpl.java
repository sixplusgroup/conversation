package finley.gmair.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import finley.gmair.dto.management.IntegralConfirmDto;
import finley.gmair.enums.membership.IntegralAddStatus;
import finley.gmair.exception.MembershipGlobalException;
import finley.gmair.dao.IntegralAddMapper;
import finley.gmair.model.membership.IntegralAdd;
import finley.gmair.param.management.IntegralConfirmParam;
import finley.gmair.service.IntegralAddService;
import finley.gmair.service.MembershipService;
import finley.gmair.util.PaginationAdapter;
import finley.gmair.util.PaginationParam;
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
        if(integralAdd.getIsConfirmed()||integralAdd.getStatus().equals(IntegralAddStatus.CONFIRMED.value())){
            throw new MembershipGlobalException("this record is already confirmed!");
        }
        if(integralAdd.getStatus().equals(IntegralAddStatus.CLOSED.value())){
            throw new MembershipGlobalException("this record is already closed!");
        }
        integralAdd.setIsConfirmed(true);
        integralAdd.setConfirmedTime(new Date());
        integralAdd.setStatus(IntegralAddStatus.CONFIRMED.value());
        integralAddMapper.updateById(integralAdd);
        membershipService.addIntegral(integralAdd.getMembershipUserId(),integralAdd.getIntegralValue());
    }

    @Override
    public PaginationParam<IntegralConfirmDto> getConfirmPage(IntegralConfirmParam integralConfirmParam, PaginationParam<IntegralConfirmDto> paginationParam) {
        paginationParam.setRecords(integralAddMapper.listConfirmPage(integralConfirmParam,new PaginationAdapter(paginationParam)));
        paginationParam.setTotal(integralAddMapper.countConfirmPage(integralConfirmParam));
        return paginationParam;
    }

    @Override
    public IntegralConfirmDto getIntegralConfirmById(String id) {
        IntegralConfirmDto integralConfirmDto =  integralAddMapper.getIntegralConfirmById(id);
        if(integralConfirmDto == null){
            throw new MembershipGlobalException("can not find this record!");
        }
        return integralConfirmDto;

    }

    @Override
    public void giveIntegral(Long id, Integer integralValue) {
        IntegralAdd integralAdd = integralAddMapper.selectById(id);
        if(integralAdd.getIsConfirmed()||integralAdd.getStatus().equals(IntegralAddStatus.CONFIRMED.value())){
            throw new MembershipGlobalException("this record is already confirmed!");
        }
        if(integralAdd.getStatus().equals(IntegralAddStatus.CLOSED.value())){
            throw new MembershipGlobalException("this record is already closed!");
        }
        integralAdd.setIntegralValue(integralValue);
        integralAdd.setStatus(IntegralAddStatus.GIVED.value());
        integralAddMapper.updateById(integralAdd);
    }

    @Override
    public void deleteById(String id) {
       IntegralAdd integralAdd = integralAddMapper.selectById(id);
       if(integralAdd!=null&&integralAdd.getIsConfirmed()){
           throw new MembershipGlobalException("this record is already confirmed!");
       }
       int num = integralAddMapper.deleteById(id);
       if(num==0){
           throw new MembershipGlobalException("can not find this record or delete failed!");
       }
    }

    @Override
    public void closeIntegralById(String id) {
        IntegralAdd integralAdd = integralAddMapper.selectById(id);
        if(integralAdd.getIsConfirmed()||integralAdd.getStatus().equals(IntegralAddStatus.CONFIRMED.value())){
            throw new MembershipGlobalException("this record is already confirmed!");
        }
        if(integralAdd.getStatus().equals(IntegralAddStatus.CLOSED.value())){
            throw new MembershipGlobalException("this record is already closed!");
        }
        integralAdd.setConfirmedTime(new Date());
        integralAdd.setStatus(IntegralAddStatus.CLOSED.value());
        integralAddMapper.updateById(integralAdd);
    }
}
