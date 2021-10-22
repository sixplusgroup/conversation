package finley.gmair.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

import finley.gmair.dto.installation.IntegralConfirmDto;
import finley.gmair.dto.membership.IntegralRecordDto;
import finley.gmair.exception.MembershipGlobalException;
import finley.gmair.model.membership.IntegralAdd;
import finley.gmair.model.membership.IntegralRecord;
import finley.gmair.model.membership.MembershipUser;
import finley.gmair.param.installation.IntegralConfirmParam;
import finley.gmair.param.installation.IntegralRecordParam;
import finley.gmair.param.membership.*;
import finley.gmair.service.IntegralAddService;
import finley.gmair.service.IntegralRecordService;
import finley.gmair.service.MembershipService;

import finley.gmair.util.PaginationParam;
import finley.gmair.util.ResponseData;
import finley.gmair.util.ResultData;
import lombok.AllArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author Joby
 */
@RestController
@AllArgsConstructor
@RequestMapping("/membership/integral")
@Validated
public class IntegralController {

    private final IntegralAddService integralAddService;

    private final MembershipService membershipService;

    private final IntegralRecordService integralRecordService;

    private final MapperFacade mapperFacade;

    /**
     * @Description directly add integral, don't need to confirm
     * @Date  2021/10/13 21:25 
     * @param params: 
     * @return finley.gmair.util.ResponseData<java.lang.Void>
     */
    @PostMapping("/deposit")
    @Transactional(rollbackFor = Exception.class)
    public ResponseData<Void> deposit(@Valid @RequestBody IntegralDepositParam params){
        Integer integral = params.getIntegral();
        String consumerId = params.getConsumerId();
        String description = "【手动添加】"+ params.getDescription();
        if(integral==null||integral<0){
            throw new MembershipGlobalException("the data is invalid, please try again!");
        }
        if(integral>10000){
            throw new MembershipGlobalException("integral is too large in one time!");
        }
        // create integralAdd record
        IntegralAdd integralAdd = new IntegralAdd();
        integralAdd.setIsConfirmed(false);
        integralAdd.setMembershipUserId(membershipService.getMembershipIdByConsumerId(consumerId));
        integralAdd.setIntegralValue(integral);
        integralAdd.setDescription(description);
        integralAddService.createAdd(integralAdd);
        // directly confirm this integralAdd record
        integralAddService.confirmIntegralById(integralAdd.getId());
        // log integral operation
        IntegralAdd confirmIntegralAdd = integralAddService.getById(integralAdd.getId());
        IntegralRecord integralRecord = new IntegralRecord();
        integralRecord.setIsAdd(true);
        integralRecord.setDescription(confirmIntegralAdd.getDescription());
        integralRecord.setIntegralValue(confirmIntegralAdd.getIntegralValue());
        integralRecord.setMembershipUserId(confirmIntegralAdd.getMembershipUserId());
        integralRecordService.createRecord(integralRecord);
        return ResponseData.ok();
    }

    @PostMapping("/createIntegralAdd")
    @Transactional(rollbackFor = Exception.class)
    public ResponseData<Void> createIntegralAdd(@Valid @RequestBody SupplementaryIntegralParam params){
        String consumerId = params.getConsumerId();
        IntegralAdd integralAdd = new IntegralAdd();
        integralAdd.setIsConfirmed(false);
        integralAdd.setDescription(params.getDescription());
        integralAdd.setMembershipUserId(membershipService.getMembershipIdByConsumerId(consumerId));
        integralAdd.setDeviceModel(params.getDeviceModel());
        integralAdd.setPictures(params.getPictures());
        integralAddService.createAdd(integralAdd);
        return ResponseData.ok();
    }

    @PostMapping("/giveIntegralOfIntegralAdd")
    @Transactional(rollbackFor = Exception.class)
    public ResponseData<Void> giveIntegralOfIntegralAdd(@Valid @RequestBody GiveIntegralParam params){
        IntegralAdd integralAdd = integralAddService.getById(params.getId());
        if(ObjectUtil.isNull(params.getId())||integralAdd==null){
            throw new MembershipGlobalException("can not find this record!");
        }
        Integer integral = params.getIntegralValue();
        if(integral==null||integral>10000||integral<0){
            throw new MembershipGlobalException("integral is too large in one time!");
        }
        if(integralAdd.getIsConfirmed()){
            throw new MembershipGlobalException("this record is already confirmed!");
        }
        integralAdd.setIntegralValue(integral);
        integralAddService.updateById(integralAdd);
        return ResponseData.ok();
    }


    @PostMapping(value = "/withdrawIntegral")
    @Transactional(rollbackFor = Exception.class)
    public ResponseData<Void> withdrawIntegral(@Valid @RequestBody IntegralWithdrawParam params){
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

        return ResponseData.ok();
    }

    @PostMapping("/getIntegral")
    public ResponseData<Integer> getMembershipIntegral(@NotBlank String consumerId){
        MembershipUser membershipUser = membershipService.getMembershipByConsumerId(consumerId);
        return ResponseData.ok(membershipUser.getIntegral());
    }

    @PostMapping("/getIntegralRecords")
    public ResponseData<List<IntegralRecordDto>> getIntegralRecords(@NotBlank String consumerId){
        List<IntegralRecord> integralRecords = integralRecordService.getMyRecordsByConsumerId(consumerId);
        return ResponseData.ok(mapperFacade.mapAsList(integralRecords, IntegralRecordDto.class));
    }

    @PostMapping("/confirmIntegral")
    @Transactional(rollbackFor = Exception.class)
    public ResponseData<Void> confirmIntegral(@Valid @RequestBody ConfirmIntegralParam confirmIntegralParam){
        Long id = confirmIntegralParam.getId();
        if(ObjectUtil.isNull(id)||integralAddService.getById(id)==null){
            throw new MembershipGlobalException("can not find this record!");
        }
        integralAddService.confirmIntegralById(id);

        // log integral operation
        IntegralAdd integralAdd = integralAddService.getById(id);
        IntegralRecord integralRecord = new IntegralRecord();
        integralRecord.setIsAdd(true);
        integralRecord.setDescription(integralAdd.getDescription());
        integralRecord.setIntegralValue(integralAdd.getIntegralValue());
        integralRecord.setMembershipUserId(integralAdd.getMembershipUserId());
        integralRecordService.createRecord(integralRecord);
        return ResponseData.ok();
    }

    @GetMapping("/getAllIntegralRecord/page")
    public ResponseData<PaginationParam<IntegralRecordDto>> getAllintegralRecords(IntegralRecordParam integralRecordParam,PaginationParam<IntegralRecordDto> paginationParam){
        integralRecordParam.setBlockFlag(false);
        PaginationParam<IntegralRecordDto> integralRecordList = integralRecordService.getRecordPage(integralRecordParam,paginationParam);
        return ResponseData.ok(integralRecordList);
    }
    @GetMapping("/getAllIntegralConfirm/page")
    public ResponseData<PaginationParam<IntegralConfirmDto>> getAllIntegralConfirm(IntegralConfirmParam integralConfirmParam, PaginationParam<IntegralConfirmDto> paginationParam){
        integralConfirmParam.setBlockFlag(false);
        PaginationParam<IntegralConfirmDto> integralConfirmList = integralAddService.getConfirmPage(integralConfirmParam,paginationParam);
        return ResponseData.ok(integralConfirmList);
    }

    @GetMapping("/getIntegralConfirm")
    public ResponseData<IntegralConfirmDto> getIntegralConfirm(@NotBlank String id){
        IntegralConfirmDto integralConfirmDto = integralAddService.getIntegralConfirmById(id);

        return ResponseData.ok(integralConfirmDto);
    }

    @GetMapping("deleteIntegralConfirm")
    public ResponseData<Void> deleteIntegralConfirm(@NotBlank String id){
        integralAddService.deleteById(id);
        return ResponseData.ok();
    }
}
