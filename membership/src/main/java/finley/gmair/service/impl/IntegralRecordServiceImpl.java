package finley.gmair.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import finley.gmair.bean.exception.MembershipGlobalException;
import finley.gmair.dao.IntegralRecordMapper;
import finley.gmair.model.IntegralRecord;
import finley.gmair.model.MembershipUser;
import finley.gmair.service.IntegralRecordService;
import finley.gmair.service.MembershipService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author Joby
 */
@Service
@AllArgsConstructor
public class IntegralRecordServiceImpl extends ServiceImpl<IntegralRecordMapper, IntegralRecord> implements IntegralRecordService {

    private final IntegralRecordMapper integralRecordMapper;

    private final MembershipService membershipService;

    @Override
    public void createRecord(IntegralRecord integralRecord) {
        if(membershipService.getMembershipById(integralRecord.getMembershipUserId())==null){
            throw new MembershipGlobalException("can not find the membership!");
        }
        integralRecordMapper.insert(integralRecord);
    }

    @Override
    public List<IntegralRecord> getMyRecordsByConsumerId(String consumerId) {
        MembershipUser membershipUser = membershipService.getMembershipByConsumerId(consumerId);
        return integralRecordMapper.selectList(new LambdaQueryWrapper<IntegralRecord>().eq(IntegralRecord::getMembershipUserId,membershipUser.getId()).orderByDesc(IntegralRecord::getCreateTime));
    }
}
