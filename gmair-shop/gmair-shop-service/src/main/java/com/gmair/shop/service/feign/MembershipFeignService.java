package com.gmair.shop.service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author Joby
 */

@FeignClient("membership")
public interface MembershipFeignService {

    @PostMapping("/p/membership/enroll")
    ResponseEntity<Void> enrollMembership(@RequestParam("consumerId") String consumerId);

}
