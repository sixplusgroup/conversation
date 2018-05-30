package finley.gmair.controller;


import finley.gmair.service.MachineService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/order/machine")
public class MachineController {

    @Autowired
    MachineService machineService;

    @RequestMapping(value = "/installType", method = RequestMethod.GET)
    public ResultData getMachineInstallType() {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        ResultData response = machineService.fetchInstallType(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
        } else {
            result.setDescription(response.getDescription());
            result.setResponseCode(response.getResponseCode());
        }
        return response;
    }

    @RequestMapping(value = "/setupProvider", method = RequestMethod.GET)
    public ResultData getMachineSetupProvider() {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
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
