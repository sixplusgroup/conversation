package com.gmair.shop.admin.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.gmair.shop.bean.model.IntegralAdd;
import com.gmair.shop.bean.model.IntegralRecord;
import com.gmair.shop.common.annotation.SysLog;
import com.gmair.shop.common.exception.GmairShopGlobalException;
import com.gmair.shop.service.IntegralAddService;
import com.gmair.shop.service.IntegralRecordService;
import com.gmair.shop.service.MembershipService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author Joby
 */
@RestController
@AllArgsConstructor
@RequestMapping("integral/integral")
public class IntegralController {

    private final IntegralAddService integralAddService;

    private final MembershipService membershipService;

    private final ThreadPoolExecutor shopPool;

    private final IntegralRecordService integralRecordService;

    @PostMapping("/confirmIntegral")
    @Transactional(rollbackFor = Exception.class)
//    @SysLog("积分确认")
    public ResponseEntity<Void> confirmIntegral(Long integralAddId){
        if(ObjectUtil.isNull(integralAddId)||integralAddService.getById(integralAddId)==null){
            throw new GmairShopGlobalException("can not find this record!");
        }
        integralAddService.confirmIntegralById(integralAddId);

        // log integral operation
        IntegralAdd integralAdd = integralAddService.getById(integralAddId);
        IntegralRecord integralRecord = new IntegralRecord();
        integralRecord.setIsAdd(true);
        integralRecord.setDescription(integralAdd.getDescription());
        integralRecord.setIntegralValue(integralAdd.getIntegralValue());
        integralRecord.setMembershipUserId(integralAdd.getMembershipUserId());
        integralRecordService.createRecord(integralRecord);

        return ResponseEntity.ok().build();
    }


}
