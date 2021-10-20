package finley.gmair.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import finley.gmair.dao.MembershipMapper;
import finley.gmair.dto.membership.IntegralRecordDto;
import finley.gmair.exception.MembershipGlobalException;
import finley.gmair.dao.IntegralRecordMapper;
import finley.gmair.model.membership.IntegralRecord;
import finley.gmair.model.membership.MembershipUser;
import finley.gmair.param.installation.IntegralRecordParam;
import finley.gmair.service.IntegralRecordService;
import finley.gmair.service.MembershipConfigService;
import finley.gmair.service.MembershipService;
import finley.gmair.util.PaginationAdapter;
import finley.gmair.util.PaginationParam;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author Joby
 */
@Service
public class IntegralRecordServiceImpl extends ServiceImpl<IntegralRecordMapper, IntegralRecord> implements IntegralRecordService {

    private final IntegralRecordMapper integralRecordMapper;
    // Use constructor injection
    public IntegralRecordServiceImpl(IntegralRecordMapper integralRecordMapper) {
        this.integralRecordMapper = integralRecordMapper;
    }
    //Prevent service cycle dependency
    @Lazy
    @Autowired
    private MembershipService membershipService;

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

    @Override
    public PaginationParam<IntegralRecordDto> getRecordPage(IntegralRecordParam integralRecordParam, PaginationParam<IntegralRecordDto> paginationParam) {
        paginationParam.setRecords(integralRecordMapper.listRecordPage(integralRecordParam,new PaginationAdapter(paginationParam)));
        paginationParam.setTotal(integralRecordMapper.countRecordPage(integralRecordParam));
        return paginationParam;
    }
}
