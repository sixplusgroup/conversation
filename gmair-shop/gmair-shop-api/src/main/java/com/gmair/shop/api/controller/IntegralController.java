package com.gmair.shop.api.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

import com.gmair.shop.bean.app.dto.IntegralRecordDto;
import com.gmair.shop.bean.app.param.IntegralDepositParam;
import com.gmair.shop.bean.model.IntegralAdd;
import com.gmair.shop.bean.model.IntegralRecord;
import com.gmair.shop.bean.model.MembershipUser;
import com.gmair.shop.common.exception.GmairShopGlobalException;
import com.gmair.shop.security.util.SecurityUtils;
import com.gmair.shop.service.IntegralAddService;
import com.gmair.shop.service.IntegralRecordService;
import com.gmair.shop.service.MembershipService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author Joby
 */
@RestController
@AllArgsConstructor
@RequestMapping("p/integral")
@Api(tags = "积分操作接口")
public class IntegralController {

    private final IntegralAddService integralAddService;

    private final MembershipService membershipService;

    private final ThreadPoolExecutor shopPool;

    private final IntegralRecordService integralRecordService;

    private final MapperFacade mapperFacade;

    @PostMapping("/deposit")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<Void> deposit(@Valid @RequestBody IntegralDepositParam params){
        Integer integral = params.getIntegral();
        String description = params.getDescription();
        if(integral==null||integral<0){
            throw new GmairShopGlobalException("the data is invalid, please try again!");
        }
        if(integral>10000){
            throw new GmairShopGlobalException("integral is too large in one time!");
        }
        if(StrUtil.isNullOrUndefined(description)||description.length()>80){
            throw new GmairShopGlobalException("the description is invalid!");
        }
        String userId = SecurityUtils.getUser().getUserId();
        IntegralAdd integralAdd = new IntegralAdd();
        integralAdd.setConfirmed(false);
        integralAdd.setMembershipUserId(membershipService.getMembershipIdByUserId(userId));
        integralAdd.setIntegralValue(integral);
        integralAdd.setDescription(description);
        integralAddService.createAdd(integralAdd);

        return ResponseEntity.ok().build();
    }


    @PostMapping(value = "/withdrawIntegral")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<Void> withdrawIntegral(Long membershipId,Integer integral,String description){
        if(membershipId==null||integral==null||membershipId<0||integral<0){
            throw new GmairShopGlobalException("the parameters are invalid!");
        }
        membershipService.withdrawIntegralById(membershipId,integral);

        // log the integral operation
        IntegralRecord integralRecord = new IntegralRecord();
        integralRecord.setAdd(false);
        integralRecord.setDescription(description);
        integralRecord.setIntegralValue(integral);
        integralRecord.setMembershipUserId(membershipId);
        integralRecordService.createRecord(integralRecord);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/getIntegral")
    public ResponseEntity<Integer> getMembershipIntegral(){
        String userId = SecurityUtils.getUser().getUserId();
        MembershipUser membershipUser = membershipService.getMembershipByUserId(userId);
        return ResponseEntity.ok(membershipUser.getIntegral());
    }

    @PostMapping("/getIntegralRecords")
    public ResponseEntity<List<IntegralRecordDto>> getIntegralRecords(){
        String userId = SecurityUtils.getUser().getUserId();
        List<IntegralRecord> integralRecords = integralRecordService.getMyRecordsByUserId(userId);
        return ResponseEntity.ok(mapperFacade.mapAsList(integralRecords,IntegralRecordDto.class));

    }


}
