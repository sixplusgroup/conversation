package finley.gmair.controller;

import finley.gmair.service.BindVersionService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/preparation/machine")

public class MachineController {

    @Autowired
    private BindVersionService bindVersionService;

    @RequestMapping(method = RequestMethod.POST, value = "/bind")
    public ResultData bindVersion(String machineId, int version, String codeValue) {
        ResultData result = new ResultData();

        //check empty input
        if (StringUtils.isEmpty(machineId)||StringUtils.isEmpty(version)||StringUtils.isEmpty(codeValue)) {
            result.setDescription("please provide all the information");
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return result;
        }

        //check if the qrcode exist
        ResultData response = bindVersionService.checkQRcodeExist(codeValue);
        if(response.getResponseCode() == ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("server is busy");
            return result;
        }else if(response.getResponseCode() == ResponseCode.RESPONSE_NULL){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("the codeValue is not exist");
            return result;
        }

        //check if the qrcode has been binded with machine(check the preBind)
        response = bindVersionService.findMachineIdByCodeValue(codeValue);
        if(response.getResponseCode() == ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("server is busy");
            return result;
        }else if(response.getResponseCode() == ResponseCode.RESPONSE_OK){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("the codeValue has binded with machine");
            return result;
        }

        //bind the machineId with version
        response = bindVersionService.recordSingleBoardVersion(machineId,version);
        if(response.getResponseCode() == ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("error to bind the machineId with version");
            return result;
        }

        //bind the machineId with codeValue
        response = bindVersionService.preBind(machineId,codeValue);
        if(response.getResponseCode() == ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("error to bind the machineId with codeValue");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setDescription("success to bind!!");
        return result;

    }
}
