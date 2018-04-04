package finley.gmair.controller;


import finley.gmair.service.MachineService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController("/order")
public class MachineController {

    @Autowired
    MachineService machineService;

    @CrossOrigin
    @RequestMapping (value = "/machine/installType", method = RequestMethod.GET)
    ResultData getMachineInstallType() {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", 0);
        ResultData response = machineService.fetchInstallType(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
        } else {
            result.setDescription(response.getDescription());
            result.setResponseCode(response.getResponseCode());
        }
        return response;
    }

    @CrossOrigin
    @RequestMapping (value = "/machine/setupProvider", method = RequestMethod.GET)
    ResultData getMachineSetupProvider() {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", 0);
        ResultData response = machineService.fetchSetupProvider(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
        } else {
            result.setDescription(response.getDescription());
            result.setResponseCode(response.getResponseCode());
        }
        return response;

    }
}
