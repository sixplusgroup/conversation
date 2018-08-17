package finley.gmair.controller;

import finley.gmair.model.enterpriseselling.MerchantProfile;
import finley.gmair.service.MerchantProfileService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/merchant/profile")
public class MerchantProfileController {
    @Autowired
    private MerchantProfileService merchantProfileService;

    @PostMapping("/create")
    public ResultData createMerchantProfile(String merchantName,String merchantCode,String merchantAddress){
        ResultData result = new ResultData();
        if(StringUtils.isEmpty(merchantName)||StringUtils.isEmpty(merchantCode)||StringUtils.isEmpty(merchantAddress)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide correct input");
            return result;
        }
        ResultData response = merchantProfileService.create(new MerchantProfile(merchantName,merchantCode,merchantAddress));
        if(response.getResponseCode()==ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to create merchant profile");
            return result;
        }
        result.setDescription("success to create merchan profile");
        return result;
    }

    @GetMapping("/fetch/by/merchantid")
    public ResultData fetchMerchantProfileByMerchantId(String merchantId){
        ResultData result = new ResultData();
        if(StringUtils.isEmpty(merchantId)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide the merchantId");
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("merchantId",merchantId);
        condition.put("blockFlag",false);
        ResultData response = merchantProfileService.fetch(condition);
        switch (response.getResponseCode()){
            case RESPONSE_ERROR:
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("fail to fetch merchant profile");
                return result;
            case RESPONSE_NULL:
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
                result.setDescription("not find merchant profile");
                return result;
            case RESPONSE_OK:
                result.setResponseCode(ResponseCode.RESPONSE_OK);
                result.setDescription("success to find merchant profile");
                result.setData(response.getData());
                return result;
        }
        return result;
    }

    @GetMapping("/fetch/list")
    public ResultData fetchMerchantProfileList(){
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag",false);
        ResultData response = merchantProfileService.fetch(condition);
        switch (response.getResponseCode()){
            case RESPONSE_ERROR:
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("fail to fetch merchant profile list");
                return result;
            case RESPONSE_NULL:
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
                result.setDescription("not find merchant profile list");
                return result;
            case RESPONSE_OK:
                result.setResponseCode(ResponseCode.RESPONSE_OK);
                result.setDescription("success to find merchant profile list");
                result.setData(response.getData());
                return result;
        }
        return result;
    }
}
