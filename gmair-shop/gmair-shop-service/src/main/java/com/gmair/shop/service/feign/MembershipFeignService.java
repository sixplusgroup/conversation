package com.gmair.shop.service.feign;

import com.gmair.shop.bean.app.dto.IntegralRecordDto;
import finley.gmair.param.membership.IntegralDepositParam;
import finley.gmair.param.membership.IntegralWithdrawParam;
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
    ResponseEntity<Void> enrollMembership(@RequestParam("consumerId") String consumerId);

    @PostMapping("/membership/integral/deposit")
    ResponseEntity<Void> deposit(@RequestBody IntegralDepositParam param);

    @PostMapping("/membership/integral/withdrawIntegral")
    ResponseEntity<Void> withdrawIntegral(@RequestBody IntegralWithdrawParam param);

    @PostMapping("/membership/integral/getIntegral")
    ResponseEntity<Integer> getMembershipIntegral(@RequestBody String consumerId);

    @PostMapping("/membership/integral/getIntegralRecords")
    ResponseEntity<List<IntegralRecordDto>> getIntegralRecords(@RequestBody String consumerId);

}
