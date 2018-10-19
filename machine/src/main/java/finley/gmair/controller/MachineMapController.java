package finley.gmair.controller;

import finley.gmair.model.machine.MachineLoc;
import finley.gmair.service.MachineMapService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/machine/map")
public class MachineMapController {

    @Autowired
    private MachineMapService machineMapService;

    @GetMapping("/fetch")
    public ResultData fetchMachineLatLngList() {
        ResultData result = new ResultData();
        ResultData response = machineMapService.fetchMachineMapList(new HashMap<>());
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setData(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to fetch the machine location map list");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setData(ResponseCode.RESPONSE_NULL);
            result.setDescription("not find any data");
            return result;
        }
        result.setData(response.getData());
        result.setDescription("success to fetch data,list.size = " + ((List<MachineLoc>) response.getData()).size());
        return result;
    }
}
