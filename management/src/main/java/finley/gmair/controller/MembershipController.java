package finley.gmair.controller;

import finley.gmair.dto.management.MembershipUserDto;
import finley.gmair.param.management.MembershipParam;
import finley.gmair.service.feign.MembershipFeignService;
import finley.gmair.util.PaginationParam;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResponseData;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Joby
 * @Date 10/18/2021 5:16 PM
 * @Description
 */
@RestController
@AllArgsConstructor
@RequestMapping("/management/membership")
public class MembershipController {

    private final MembershipFeignService membershipFeignService;

    @GetMapping("/list")
    public ResponseData<PaginationParam<MembershipUserDto>> getMembershipList(MembershipParam membershipParam, PaginationParam<MembershipUserDto> page){
        ResponseData<PaginationParam<MembershipUserDto>> responseData;
        responseData = membershipFeignService.getMembershipList(membershipParam.getId(),membershipParam.getMembershipType(),membershipParam.getUserMobile(),membershipParam.getConsumerName(),page.getSize(),page.getCurrent(),page.getCreateTimeSort());
        if(responseData.getResponseCode()!= ResponseCode.RESPONSE_OK){
            return ResponseData.error("get membership list failed!");
        }
        return responseData;
    }
    @GetMapping("/delete")
    public ResponseData<Void> deleteMembership(String id){
        ResponseData<Void> responseData = membershipFeignService.deleteMembership(id);
        if(responseData.getResponseCode()!=ResponseCode.RESPONSE_OK){
            return ResponseData.error(responseData.getDescription());
        }
        return responseData;
    }
}
