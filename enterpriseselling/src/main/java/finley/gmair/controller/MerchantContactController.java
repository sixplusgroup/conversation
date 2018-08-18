package finley.gmair.controller;

import finley.gmair.model.enterpriseselling.MerchantContact;
import finley.gmair.service.MerchantContactService;
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
@RequestMapping("/merchant/contact")
public class MerchantContactController {
    @Autowired
    private MerchantContactService merchantContactService;

    @PostMapping("/create")
    public ResultData createMerchantContact(String merchantId, String contactName, String contactPhone, boolean preferred){
        ResultData result = new ResultData();
        if(StringUtils.isEmpty(merchantId)||StringUtils.isEmpty(contactName)||StringUtils.isEmpty(contactPhone)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide correct input");
            return result;
        }
        ResultData response = merchantContactService.create(new MerchantContact(merchantId,contactName,contactPhone,preferred));
        if(response.getResponseCode()==ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to create merchant contact");
            return result;
        }
        result.setDescription("success to create merchan contact");
        return result;
    }

    @GetMapping("/fetch/by/contactid")
    public ResultData fetchMerchantContactByContactId(String contactId){
        ResultData result = new ResultData();
        if(StringUtils.isEmpty(contactId)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide the contactId");
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("contactId",contactId);
        condition.put("blockFlag",false);
        ResultData response = merchantContactService.fetch(condition);
        switch (response.getResponseCode()){
            case RESPONSE_ERROR:
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("fail to fetch merchant contact");
                return result;
            case RESPONSE_NULL:
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
                result.setDescription("not find merchant contact");
                return result;
            case RESPONSE_OK:
                result.setResponseCode(ResponseCode.RESPONSE_OK);
                result.setDescription("success to find merchant contact");
                result.setData(response.getData());
                return result;
        }
        return result;
    }

    @GetMapping("/fetch/list")
    public ResultData fetchMerchantContactList(){
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag",false);
        ResultData response = merchantContactService.fetch(condition);
        switch (response.getResponseCode()){
            case RESPONSE_ERROR:
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("fail to fetch merchant contact list");
                return result;
            case RESPONSE_NULL:
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
                result.setDescription("not find merchant contact list");
                return result;
            case RESPONSE_OK:
                result.setResponseCode(ResponseCode.RESPONSE_OK);
                result.setDescription("success to find merchant contact list");
                result.setData(response.getData());
                return result;
        }
        return result;
    }
}
