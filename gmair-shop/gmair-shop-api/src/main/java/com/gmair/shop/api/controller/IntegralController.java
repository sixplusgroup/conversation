package com.gmair.shop.api.controller;

import com.gmair.shop.bean.app.dto.IntegralRecordDto;
import com.gmair.shop.bean.app.param.PSupplementaryIntegralParam;
import com.gmair.shop.bean.app.param.PIntegralWithdrawParam;
import com.gmair.shop.bean.model.User;
import com.gmair.shop.common.exception.GmairShopGlobalException;
import com.gmair.shop.security.util.SecurityUtils;
import com.gmair.shop.service.UserService;
import com.gmair.shop.service.feign.MembershipFeignService;
import com.gmair.shop.service.feign.ResourceFeignService;
import finley.gmair.param.membership.IntegralWithdrawParam;
import finley.gmair.param.membership.SupplementaryIntegralParam;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResponseData;
import finley.gmair.util.ResultData;
import lombok.AllArgsConstructor;
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
@RequestMapping("/shop/consumer/integral")
@AllArgsConstructor
public class IntegralController {

    private final UserService userService;

    private final MembershipFeignService membershipFeignService;

    private final ResourceFeignService resourceFeignService;

    @PostMapping("/supplementaryIntegral")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<Void> supplementaryIntegral(@Valid @RequestBody PSupplementaryIntegralParam params){
        String userId = SecurityUtils.getUser().getUserId();
        User user = userService.getUserByUserId(userId);
        SupplementaryIntegralParam supplementaryIntegralParam = new SupplementaryIntegralParam();
        supplementaryIntegralParam.setConsumerId(user.getConsumerId());
        supplementaryIntegralParam.setDescription(params.getDescription());
        supplementaryIntegralParam.setDeviceModel(params.getDeviceModel());
        supplementaryIntegralParam.setPictures(params.getPictures());
        ResponseData<Void> responseData = membershipFeignService.createIntegralAdd(supplementaryIntegralParam);

        ResultData resultData = resourceFeignService.save(params.getPictures());
        if(resultData.getResponseCode()!=ResponseCode.RESPONSE_OK){
            throw new GmairShopGlobalException(resultData.getDescription());
        }
        if(responseData.getResponseCode()== ResponseCode.RESPONSE_OK){
            return ResponseEntity.ok().build();
        }else{
            throw new GmairShopGlobalException(responseData.getDescription());
        }
    }

    @PostMapping(value = "/withdrawIntegral")
    public ResponseEntity<Void> withdrawIntegral(@Valid @RequestBody PIntegralWithdrawParam params){
        String userId = SecurityUtils.getUser().getUserId();
        User user = userService.getUserByUserId(userId);
        IntegralWithdrawParam integralWithdrawParam = new IntegralWithdrawParam();
        integralWithdrawParam.setConsumerId(user.getConsumerId());
        integralWithdrawParam.setDescription(params.getDescription());
        integralWithdrawParam.setIntegral(params.getIntegral());
        ResponseData<Void> responseData =  membershipFeignService.withdrawIntegral(integralWithdrawParam);

        if(responseData.getResponseCode()== ResponseCode.RESPONSE_OK){
            return ResponseEntity.ok().build();
        }else{
            throw new GmairShopGlobalException(responseData.getDescription());
        }
    }

    @PostMapping("/getIntegral")
    public ResponseEntity<Integer> getMembershipIntegral(){
        String userId = SecurityUtils.getUser().getUserId();
        User user = userService.getUserByUserId(userId);
        ResponseData<Integer> responseData = membershipFeignService.getMembershipIntegral(user.getConsumerId());
        if(responseData.getResponseCode()== ResponseCode.RESPONSE_OK){
            return ResponseEntity.ok(responseData.getData());
        }else{
            throw new GmairShopGlobalException(responseData.getDescription());
        }
    }

    @PostMapping("/getIntegralRecords")
    public ResponseEntity<List<IntegralRecordDto>> getIntegralRecords(){
        String userId = SecurityUtils.getUser().getUserId();
        User user = userService.getUserByUserId(userId);
        ResponseData<List<IntegralRecordDto>> responseData = membershipFeignService.getIntegralRecords(user.getConsumerId());
        if(responseData.getResponseCode()== ResponseCode.RESPONSE_OK){
            return ResponseEntity.ok(responseData.getData());
        }else{
            throw new GmairShopGlobalException(responseData.getDescription());
        }
    }
}
