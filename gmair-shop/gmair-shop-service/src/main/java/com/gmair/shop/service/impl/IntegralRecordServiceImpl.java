package com.gmair.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gmair.shop.bean.model.IntegralAdd;
import com.gmair.shop.bean.model.IntegralRecord;
import com.gmair.shop.bean.model.MembershipUser;
import com.gmair.shop.common.exception.GmairShopGlobalException;
import com.gmair.shop.dao.IntegralRecordMapper;
import com.gmair.shop.service.IntegralRecordService;
import com.gmair.shop.service.MembershipService;
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
            throw new GmairShopGlobalException("can not find the membership!");
        }
        integralRecordMapper.insert(integralRecord);
    }

    @Override
    public List<IntegralRecord> getMyRecordsByUserId(String userId) {
        MembershipUser membershipUser = membershipService.getMembershipByUserId(userId);
        return integralRecordMapper.selectList(new LambdaQueryWrapper<IntegralRecord>().eq(IntegralRecord::getMembershipUserId,membershipUser.getId()).orderByDesc(IntegralRecord::getCreateTime));
    }
}
