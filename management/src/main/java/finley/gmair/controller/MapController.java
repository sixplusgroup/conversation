package finley.gmair.controller;

import finley.gmair.service.AirQualityService;
import finley.gmair.service.FormaldehydeService;
import finley.gmair.service.MachineService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/management/map")
public class MapController {
    @Autowired
    private MachineService machineService;

    @Autowired
    private FormaldehydeService formaldehydeService;

    @Autowired
    private AirQualityService airQualityService;


    //machine map

    @GetMapping("/machine/fetch")
    public ResultData fetchMachineLatLngList() {
        return machineService.fetchMachineLatLngList();
    }

    @GetMapping("/machine/online")
    public ResultData isOnline(String codeValue) {
        return machineService.checkOnline(codeValue);
    }

    @GetMapping("/machine/status")
    public ResultData getMachineStatus(String machineId) {
        return machineService.machineStatus(machineId);
    }

    @GetMapping("/machine/model")
    public ResultData getMachineModel(String codeValue) {
        return machineService.getModel(codeValue);
    }

    @GetMapping("/machine/city/air")
    public ResultData getCityLatestAirquality(String cityId, String provinceId) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(cityId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide cityId");
            return result;
        }

        //先根据城市来查找空气质量
        ResultData response = airQualityService.getLatestCityAirQuality(cityId);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
            result.setDescription("success to fetch the air quality by cityId");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to fetch the air quality by cityId");
            return result;
        }

        //根据省份来查找空气质量
        response = airQualityService.provinceAirQuality(provinceId);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to fetch the air quality");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("not find the air quality by provinceId");
            return result;
        }
        result.setData(response.getData());
        result.setDescription("success to fetch the air quality by provinceId");
        return result;
    }



    //formaldehyde map

    @GetMapping("/formaldehyde/list/fetch")
    public ResultData getMapList(){
        return formaldehydeService.getCaseLocationList();
    }

    @GetMapping("/formaldehyde/case/profile/fetch")
    public ResultData getCaseProfile(String caseId){
        return formaldehydeService.fetchCaseProfile(caseId,null,null,null,null,null,null);
    }

    @GetMapping("/formaldehyde/check/equipment/fetch")
    public ResultData getCheckEquipment(String equipmentId){
        return formaldehydeService.getEquipmentById(equipmentId);
    }

}
