package finley.gmair.service.feign;

import finley.gmair.dto.management.MembershipUserDto;
import finley.gmair.util.PaginationParam;
import finley.gmair.util.ResponseData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author Joby
 * @Date 10/18/2021 8:35 PM
 * @Description
 */
@FeignClient("membership")
public interface MembershipFeignService {

    @GetMapping("/membership/membership/page")
    ResponseData<PaginationParam<MembershipUserDto>> getMembershipList(@RequestParam("id") Long id, @RequestParam("membershipType") Integer membershipType, @RequestParam("userMobile") String userMobile, @RequestParam("consumerName") String consumerName, @RequestParam("size") long size, @RequestParam("current") long current, @RequestParam("createTimeSort") Boolean sort);

    @GetMapping("/membership/membership/delete")
    ResponseData<Void> deleteMembership(@RequestParam("id") String id);

}

