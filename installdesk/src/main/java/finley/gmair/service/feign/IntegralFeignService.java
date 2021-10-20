package finley.gmair.service.feign;

import finley.gmair.dto.installation.MembershipUserDto;
import finley.gmair.dto.membership.IntegralRecordDto;
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
public interface IntegralFeignService {

    @GetMapping("/membership/integral/getAllIntegralRecord/page")
    ResponseData<PaginationParam<IntegralRecordDto>> getAllIntegralRecords(@RequestParam("isAdd") Boolean isAdd,@RequestParam("membershipUserId") String membershipUserId,@RequestParam("search") String search,@RequestParam("membershipType") Integer membershipType,@RequestParam("sortType") String sortType,@RequestParam("current") long current,@RequestParam("size") long size);


}

