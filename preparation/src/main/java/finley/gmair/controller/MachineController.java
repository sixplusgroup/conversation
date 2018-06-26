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

        //bind the machineId with version
        ResultData response = bindVersionService.recordSingleBoardVersion(machineId,version);
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
        return result;

    }
}
