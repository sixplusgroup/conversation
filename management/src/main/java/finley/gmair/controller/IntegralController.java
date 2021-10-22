package finley.gmair.controller;

import finley.gmair.dto.installation.IntegralConfirmDto;
import finley.gmair.dto.membership.IntegralRecordDto;
import finley.gmair.param.installation.IntegralConfirmParam;
import finley.gmair.param.installation.IntegralRecordParam;
import finley.gmair.param.membership.ConfirmIntegralParam;
import finley.gmair.param.membership.GiveIntegralParam;
import finley.gmair.service.feign.IntegralFeignService;
import finley.gmair.util.PaginationParam;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResponseData;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @Author Joby
 * @Date 10/18/2021 5:17 PM
 * @Description
 */
@RequestMapping("/management/membership/integral")
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

    @GetMapping("/integralConfirm/page")
    public ResponseData<PaginationParam<IntegralConfirmDto>> getIntegralConfirmPage(IntegralConfirmParam integralConfirmParam, PaginationParam<IntegralConfirmDto> paginationParam){
        ResponseData<PaginationParam<IntegralConfirmDto>> responseData;
        responseData = integralFeignService.getIntegralConfirms(integralConfirmParam.getIsConfirmed(),integralConfirmParam.getMembershipUserId(),integralConfirmParam.getSearch(),integralConfirmParam.getMembershipType(),integralConfirmParam.getSortType(),paginationParam.getCurrent(),paginationParam.getSize());
        if(responseData.getResponseCode()!= ResponseCode.RESPONSE_OK){
            return ResponseData.error(responseData.getDescription());
        }
        return responseData;
    }

    @GetMapping("/integralConfirm/getById")
    public ResponseData<IntegralConfirmDto> getIntegralConfirmById(String id){
        ResponseData<IntegralConfirmDto> responseData = integralFeignService.getIntegralConfirmById(id);
        if(responseData.getResponseCode()!=ResponseCode.RESPONSE_OK){
            return ResponseData.error(responseData.getDescription());
        }
        return responseData;
    }

    @GetMapping("/integralConfirm/delete")
    public ResponseData<Void> deleteIntegralConfirm(String id){
        ResponseData<Void> responseData = integralFeignService.deleteIntegralConfirmById(id);
        if(responseData.getResponseCode()!=ResponseCode.RESPONSE_OK){
            return ResponseData.error(responseData.getDescription());
        }
        return responseData;
    }

    @PostMapping("/integralConfirm/giveIntegral")
    public ResponseData<Void> giveIntegral(@RequestBody GiveIntegralParam giveIntegralParam){
        ResponseData<Void> responseData = integralFeignService.giveIntegralById(giveIntegralParam);
        if(responseData.getResponseCode()!=ResponseCode.RESPONSE_OK){
            return ResponseData.error(responseData.getDescription());
        }
        return responseData;
    }
    @PostMapping("/integralConfirm/confirmIntegral")
    public ResponseData<Void> confirmIntegral(@RequestBody ConfirmIntegralParam confirmIntegralParam){
        ResponseData<Void> responseData = integralFeignService.confirmIntegralById(confirmIntegralParam);
        if(responseData.getResponseCode()!=ResponseCode.RESPONSE_OK){
            return ResponseData.error(responseData.getDescription());
        }
        return responseData;
    }

}
