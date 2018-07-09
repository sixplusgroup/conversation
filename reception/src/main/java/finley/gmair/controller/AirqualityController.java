package finley.gmair.controller;

import finley.gmair.model.machine.v2.MachineStatus;
import finley.gmair.service.MachineService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


@RestController
@RequestMapping("/reception/airquality")
public class AirqualityController {
    @Autowired
    private MachineService machineService;

    //根据当前的qrcode查询这台机器machine status
    @RequestMapping(value = "/probe", method = RequestMethod.GET)
    public ResultData getAirQuality(String qrcode) {
        ResultData result = new ResultData();
        ResultData response = machineService.findMachineIdByCodeValue(qrcode);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("server is busy");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("can not find the qrcode");
            return result;
        }
        List<Object> preBindCodeList = (ArrayList<Object>) response.getData();
        LinkedHashMap<String, Object> linkedHashMap = (LinkedHashMap<String, Object>) (preBindCodeList.get(0));
        String machineId = (String) linkedHashMap.get("machineId");

        response = machineService.machineStatus(machineId);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription("success to get air quality");
            result.setData(response.getData());
            return result;
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("can not find");
            return result;
        }
    }
}
