package finley.gmair.controller;

import finley.gmair.model.map.MachineLoc;
import finley.gmair.service.AirqualityService;
import finley.gmair.service.MachineMapService;
import finley.gmair.service.MachineService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/map/machine")
@CrossOrigin
public class MachineMapController {
    @Autowired
    private MachineMapService machineMapService;

    @Autowired
    private MachineService machineService;

    @Autowired
    private AirqualityService airqualityService;

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

    @GetMapping("/online")
    public ResultData isOnline(String codeValue){
        return machineService.checkOnline(codeValue);
    }

    @GetMapping("/status")
    public ResultData getMachineStatus(String machineId){
        return machineService.machineStatus(machineId);
    }

    @GetMapping("/model")
    public ResultData getMachineModel(String codeValue){
        return machineService.getModel(codeValue);
    }

    @GetMapping("/city/air")
    public ResultData getCityLatestAirquality(String cityId) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(cityId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide cityId");
            return result;
        }
        return airqualityService.getLatestCityAirQuality(cityId);
    }
}
