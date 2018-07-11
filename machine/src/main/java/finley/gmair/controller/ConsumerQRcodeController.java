package finley.gmair.controller;

import finley.gmair.model.machine.ConsumerQRcodeBind;
import finley.gmair.model.machine.Ownership;
import finley.gmair.service.ConsumerQRcodeBindService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/machine/consumer")
public class ConsumerQRcodeController {
    @Autowired
    private ConsumerQRcodeBindService consumerQRcodeBindService;

    @RequestMapping(value = "/bindwithqrcode", method = RequestMethod.POST)
    public ResultData bindConsumerWithQRcode(String consumerId, String bindName, String qrcode, int ownership) {
        ResultData result = new ResultData();
        //check empty
        if (StringUtils.isEmpty(consumerId) || StringUtils.isEmpty(bindName) || StringUtils.isEmpty(qrcode) || StringUtils.isEmpty(Ownership.fromValue(ownership))) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide all the information");
            return result;
        }

        //create
        ConsumerQRcodeBind consumerQRcodeBind = new ConsumerQRcodeBind();
        consumerQRcodeBind.setConsumerId(consumerId);
        consumerQRcodeBind.setBindName(bindName.trim());
        consumerQRcodeBind.setCodeValue(qrcode);
        consumerQRcodeBind.setOwnership(Ownership.fromValue(ownership));
        ResultData response = consumerQRcodeBindService.createConsumerQRcodeBind(consumerQRcodeBind);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("server is busy");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription("success to create bind");
        }
        return result;
    }

    @RequestMapping(value = "/check/devicename/exist", method = RequestMethod.GET)
    public ResultData checkDeviceNameExist(String consumerId, String bindName, String qrcode) {
        ResultData result = new ResultData();
        //check empty
        if (StringUtils.isEmpty(consumerId) || StringUtils.isEmpty(bindName) || StringUtils.isEmpty(qrcode)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide all the information");
            return result;
        }

        //check qrcode exist
        Map<String,Object> condition = new HashMap<>();
        condition.put("codeValue",qrcode);
        condition.put("blockFlag",false);
        ResultData response = consumerQRcodeBindService.fetchConsumerQRcodeBind(condition);
        if(response.getResponseCode()==ResponseCode.RESPONSE_OK){
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription("exist qrcode");
            return result;
        }else if(response.getResponseCode()== ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("server is busy now");
            return result;
        }

        //check bindName exist
        condition.clear();
        condition.put("consumerId",consumerId);
        condition.put("bindName",bindName);
        condition.put("blockFlag",false);
        response = consumerQRcodeBindService.fetchConsumerQRcodeBind(condition);
        if(response.getResponseCode() == ResponseCode.RESPONSE_OK){
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription("exist device name");
            return result;
        }else if(response.getResponseCode() == ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("server is busy now");
            return result;
        }else{
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("this device name has not been used");
        }
        return result;

    }

    @RequestMapping(value="/machinelist", method = RequestMethod.GET)
    public ResultData getMachineListByConsumerId(String consumerId){
        ResultData result = new ResultData();
        //check empty
        if( StringUtils.isEmpty(consumerId)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide all information");
            return result;
        }

        Map<String, Object> condition = new HashMap<>();
        condition.put("consumerId",consumerId);
        condition.put("blockFlag",false);
        ResultData response = consumerQRcodeBindService.fetchConsumerQRcodeBind(condition);
        if(response.getResponseCode()==ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("server is busy now");
            return result;
        }else if(response.getResponseCode()==ResponseCode.RESPONSE_NULL){
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("not found any one");
            return result;
        }else if(response.getResponseCode()==ResponseCode.RESPONSE_OK){
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription("success");
        }
        return result;
    }

}
