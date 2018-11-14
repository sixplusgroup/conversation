package finley.gmair.controller;

import finley.gmair.service.AirqualityService;
import finley.gmair.service.MachineService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/monitor")
@CrossOrigin
public class MonitorController {

    @Autowired
    private MachineService machineService;

    @Autowired
    private AirqualityService airqualityService;

    @GetMapping("/machine/online")
    public ResultData isOnline(String codeValue) {
        return machineService.checkOnline(codeValue);
    }

    @GetMapping("/machine/status")
    public ResultData getMachineStatus(String qrcode) {
        return machineService.getMachineStatusByQRcode(qrcode);
    }

    @GetMapping("/cityid/probe")
    public ResultData getCityId(String qrcode){ return machineService.probeCityIdByQRcode(qrcode); }

    @GetMapping("/city/air")
    public ResultData getCityLatestAirquality(String cityId, String provinceId) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(cityId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide cityId");
            return result;
        }

        //先根据城市来查找空气质量
        ResultData response = airqualityService.getLatestCityAirQuality(cityId);
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
        response = airqualityService.provinceAirQuality(provinceId);
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
}
