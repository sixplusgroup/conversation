package finley.gmair.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

import finley.gmair.dto.membership.IntegralRecordDto;
import finley.gmair.exception.MembershipGlobalException;
import finley.gmair.model.membership.IntegralAdd;
import finley.gmair.model.membership.IntegralRecord;
import finley.gmair.model.membership.MembershipUser;
import finley.gmair.param.membership.IntegralDepositParam;
import finley.gmair.param.membership.IntegralWithdrawParam;
import finley.gmair.service.IntegralAddService;
import finley.gmair.service.IntegralRecordService;
import finley.gmair.service.MembershipService;

import lombok.AllArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @Author Joby
 */
@RestController
@AllArgsConstructor
@RequestMapping("/membership/integral")
public class IntegralController {

    private final IntegralAddService integralAddService;

    private final MembershipService membershipService;

    private final IntegralRecordService integralRecordService;

    private final MapperFacade mapperFacade;

    @PostMapping("/deposit")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<Void> deposit(@Valid @RequestBody IntegralDepositParam params){
        Integer integral = params.getIntegral();
        String description = params.getDescription();
        String consumerId = params.getConsumerId();

        if(integral==null||integral<0){
            throw new MembershipGlobalException("the data is invalid, please try again!");
        }
        if(integral>10000){
            throw new MembershipGlobalException("integral is too large in one time!");
        }
        if(StrUtil.isNullOrUndefined(description)||description.length()>80){
            throw new MembershipGlobalException("the description is invalid!");
        }

        IntegralAdd integralAdd = new IntegralAdd();
        integralAdd.setIsConfirmed(false);
        integralAdd.setMembershipUserId(membershipService.getMembershipIdByConsumerId(consumerId));
        integralAdd.setIntegralValue(integral);
        integralAdd.setDescription(description);
        integralAddService.createAdd(integralAdd);

        return ResponseEntity.ok().build();
    }


    @PostMapping(value = "/withdrawIntegral")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<Void> withdrawIntegral(@Valid @RequestBody IntegralWithdrawParam params){
        String consumerId = params.getConsumerId();
        String description = params.getDescription();
        Integer integral = params.getIntegral();
        if(consumerId==null||integral==null||StrUtil.isBlank(consumerId)||integral<0){
            throw new MembershipGlobalException("the parameters are invalid!");
        }
        Long membershipId = membershipService.getMembershipIdByConsumerId(consumerId);
        if(membershipId==null||membershipId==0){
            throw new MembershipGlobalException("this consumer isn't a membership!");
        }
        membershipService.withdrawIntegralById(membershipId,integral);

        // log the integral operation
        IntegralRecord integralRecord = new IntegralRecord();
        integralRecord.setIsAdd(false);
        integralRecord.setDescription(description);
        integralRecord.setIntegralValue(integral);
        integralRecord.setMembershipUserId(membershipId);
        integralRecordService.createRecord(integralRecord);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/getIntegral")
    public ResponseEntity<Integer> getMembershipIntegral(String consumerId){
        MembershipUser membershipUser = membershipService.getMembershipByConsumerId(consumerId);
        return ResponseEntity.ok(membershipUser.getIntegral());
    }

    @PostMapping("/getIntegralRecords")
    public ResponseEntity<List<IntegralRecordDto>> getIntegralRecords(String consumerId){
        List<IntegralRecord> integralRecords = integralRecordService.getMyRecordsByConsumerId(consumerId);
        return ResponseEntity.ok(mapperFacade.mapAsList(integralRecords, IntegralRecordDto.class));

    }

    @PostMapping("/confirmIntegral")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<Void> confirmIntegral(Long integralAddId){
        if(ObjectUtil.isNull(integralAddId)||integralAddService.getById(integralAddId)==null){
            throw new MembershipGlobalException("can not find this record!");
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
