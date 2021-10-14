package com.gmair.shop.service.feign;

import com.gmair.shop.bean.app.dto.IntegralRecordDto;
import com.gmair.shop.bean.app.param.PSupplementaryIntegralParam;
import finley.gmair.param.membership.IntegralDepositParam;
import finley.gmair.param.membership.IntegralWithdrawParam;
import finley.gmair.param.membership.SupplementaryIntegralParam;
import finley.gmair.util.ResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Author Joby
 */

@FeignClient("membership")
public interface MembershipFeignService {

    @PostMapping("/membership/membership/enroll")
    ResponseData<Void> enrollMembership(@RequestParam("consumerId") String consumerId);

//    @PostMapping("/membership/integral/deposit")
//    ResponseData<Void> deposit(@RequestBody IntegralDepositParam param);

    @PostMapping("/membership/integral/createIntegralAdd")
    ResponseData<Void> createIntegralAdd(@RequestBody SupplementaryIntegralParam param);

    @PostMapping("/membership/integral/withdrawIntegral")
    ResponseData<Void> withdrawIntegral(@RequestBody IntegralWithdrawParam param);

    @PostMapping("/membership/integral/getIntegral")
    ResponseData<Integer> getMembershipIntegral(@RequestParam("consumerId") String consumerId);

    @PostMapping("/membership/integral/getIntegralRecords")
    ResponseData<List<IntegralRecordDto>> getIntegralRecords(@RequestParam("consumerId") String consumerId);

    @PostMapping("/membership/membership/isMembership")
    ResponseData<Boolean> isMembership(@RequestParam("consumerId") String consumerId);

}
