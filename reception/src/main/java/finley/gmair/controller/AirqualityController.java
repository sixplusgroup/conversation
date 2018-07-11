package finley.gmair.controller;

import finley.gmair.model.machine.v2.MachineStatus;
import finley.gmair.service.AuthConsumerService;
import finley.gmair.service.MachineService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.transform.Result;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


@RestController
@RequestMapping("/reception/airquality")
public class AirqualityController {
    @Autowired
    private MachineService machineService;

    @Autowired
    private AuthConsumerService authConsumerService;
    //用户读设备列表接口
    @RequestMapping(value = "/devicelist", method = RequestMethod.GET)
    public ResultData getUserDeviceList(){
        ResultData result = new ResultData();
        String phone = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String consumerId = (String) authConsumerService.getConsumerId(phone).getData();
        ResultData response = machineService.getMachineListByConsumerId(consumerId);
        if(response.getResponseCode()==ResponseCode.RESPONSE_OK){
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription("success to get machine list");
            return result;
        }else if(response.getResponseCode()==ResponseCode.RESPONSE_NULL){
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("not find");
            return result;
        }else{
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("server is busy");
            return result;
        }
    }

}
