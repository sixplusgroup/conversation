package finley.gmair.service.feign;

import finley.gmair.dto.management.IntegralConfirmDto;
import finley.gmair.dto.membership.IntegralRecordDto;
import finley.gmair.util.PaginationParam;
import finley.gmair.util.ResponseData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author Joby
 * @Date 10/18/2021 8:35 PM
 * @Description
 */
@FeignClient("membership")
public interface IntegralFeignService {

    @GetMapping("/membership/integral/getAllIntegralRecord/page")
    ResponseData<PaginationParam<IntegralRecordDto>> getAllIntegralRecords(@RequestParam("isAdd") Boolean isAdd, @RequestParam("membershipUserId") String membershipUserId, @RequestParam("search") String search, @RequestParam("membershipType") Integer membershipType, @RequestParam("sortType") String sortType, @RequestParam("current") long current, @RequestParam("size") long size);

    @GetMapping("/membership/integral/getAllIntegralConfirm/page")
    ResponseData<PaginationParam<IntegralConfirmDto>> getIntegralConfirms(@RequestParam("isConfirmed") Boolean isConfirmed, @RequestParam("membershipUserId") String membershipUserId, @RequestParam("search") String search, @RequestParam("membershipType") Integer membershipType, @RequestParam("sortType") String sortType, @RequestParam("status") Integer status,@RequestParam("current") long current, @RequestParam("size") long size);

    @GetMapping("/membership/integral/getIntegralConfirm")
    ResponseData<IntegralConfirmDto> getIntegralConfirmById(@RequestParam("id") String id);

    @GetMapping("/membership/integral/deleteIntegralConfirm")
    ResponseData<Void> deleteIntegralConfirmById(@RequestParam("id") String id);

    @PostMapping("/membership/integral/giveIntegralOfIntegralAdd")
    ResponseData<Void> giveIntegralById(@RequestParam("id") Long id, @RequestParam("integralValue") Integer integralValue);

    @PostMapping("/membership/integral/confirmIntegral")
    ResponseData<Void> confirmIntegralById(@RequestParam("id") Long id);

    @GetMapping("/membership/integral/closeIntegral")
    ResponseData<Void> closeIntegralById(@RequestParam("id") String id);


}

