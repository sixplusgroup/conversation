package finley.gmair.controller;

import finley.gmair.dto.installation.MembershipUserDto;
import finley.gmair.dto.membership.IntegralRecordDto;
import finley.gmair.param.installation.IntegralRecordParam;
import finley.gmair.service.feign.IntegralFeignService;
import finley.gmair.util.PaginationParam;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResponseData;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Joby
 * @Date 10/18/2021 5:17 PM
 * @Description
 */
@RequestMapping("/install/integral")
@AllArgsConstructor
@RestController
public class IntegralController {

    private final IntegralFeignService integralFeignService;

    @GetMapping("/integralRecord/page")
    public ResponseData<PaginationParam<IntegralRecordDto>> getIntegralRecordPage(IntegralRecordParam integralRecordParam, PaginationParam<IntegralRecordDto> paginationParam){
        ResponseData<PaginationParam<IntegralRecordDto>> responseData;
        responseData = integralFeignService.getAllIntegralRecords(integralRecordParam.getIsAdd(),integralRecordParam.getMembershipUserId(),integralRecordParam.getSearch(),integralRecordParam.getMembershipType(),integralRecordParam.getSortType(),paginationParam.getCurrent(),paginationParam.getSize());
        if(responseData.getResponseCode()!= ResponseCode.RESPONSE_OK){
            return ResponseData.error(responseData.getDescription());
        }
        return responseData;
    }

}
