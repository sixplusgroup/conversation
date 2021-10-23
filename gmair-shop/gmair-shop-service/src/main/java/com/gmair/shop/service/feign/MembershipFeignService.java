package com.gmair.shop.service.feign;


import com.gmair.shop.bean.app.param.PSupplementaryIntegralParam;
import finley.gmair.dto.management.IntegralConfirmDto;
import finley.gmair.dto.membership.IntegralRecordDto;
import finley.gmair.param.membership.IntegralDepositParam;
import finley.gmair.param.membership.IntegralWithdrawParam;
import finley.gmair.param.membership.MembershipInfoParam;
import finley.gmair.param.membership.SupplementaryIntegralParam;
import finley.gmair.util.PaginationParam;
import finley.gmair.util.ResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/membership/membership/setMembershipInfo")
    ResponseData<Void> setMembershipInfo(@RequestBody MembershipInfoParam membershipInfoParam);

    @PostMapping("/membership/integral/createIntegralAdd")
    ResponseData<Void> createIntegralAdd(@RequestBody SupplementaryIntegralParam param);

    @PostMapping("/membership/integral/withdrawIntegral")
    ResponseData<Void> withdrawIntegral(@RequestBody IntegralWithdrawParam param);

    @PostMapping("/membership/membership/isMembership")
    ResponseData<Boolean> isMembership(@RequestParam("consumerId") String consumerId);

    @PostMapping("/membership/integral/getIntegral")
    ResponseData<Integer> getMembershipIntegral(@RequestParam("consumerId") String consumerId);

    @GetMapping("/membership/integral/getAllIntegralRecord/page")
    ResponseData<PaginationParam<IntegralRecordDto>> getIntegralRecords(@RequestParam("consumerId") String consumerId,@RequestParam("current") long current,@RequestParam("size") long size,@RequestParam("sortType")String sortType);

    @GetMapping("/membership/integral/getAllIntegralConfirm/page")
    ResponseData<PaginationParam<IntegralConfirmDto>> getIntegralAdds(@RequestParam("consumerId") String consumerId, @RequestParam("current") long current, @RequestParam("size") long size,@RequestParam("sortType")String sortType);

}
