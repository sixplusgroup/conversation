package finley.gmair.controller;

import finley.gmair.service.AirqualityService;
import finley.gmair.service.MachineService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;


@RestController
@RequestMapping("/reception/airquality")
public class AirqualityController {
    @Autowired
    private MachineService machineService;

    @Autowired
    private AirqualityService airqualityService;

    //根据qrcode获取那个城市最新的空气质量记录(包括pm2.5、pm10、co等等)
    @RequestMapping(value = "/city", method = RequestMethod.GET)
    ResultData getCityLatestAirquality(String cityId) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(cityId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide cityId");
            return result;
        }
        return airqualityService.getLatestCityAirQuality(cityId);
    }

    //根据cityId获取那个城市最新的空气质量记录(包括pm2.5、pm10、co等等),并修改qrcode对应机器的默认城市位置
    @RequestMapping(value = "/city/modify", method = RequestMethod.POST)
    public ResultData getCityLatestAqiAndModifyDefaultCity(String qrcode, String cityId) {
        ResultData result = new ResultData();
        //check empty
        if (StringUtils.isEmpty(qrcode) || StringUtils.isEmpty(cityId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide all the information");
            return result;
        }
        ResultData response = machineService.updateCityIdByQRcode(cityId, qrcode);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        }
        return result;
    }

    //根据cityId给出那个城市过去24小时的pm2.5记录
    @RequestMapping(value = "/city/hourly/aqi", method = RequestMethod.GET)
    ResultData getCityHourlyAqi(String cityId) {
        ResultData result = new ResultData();
        //check empty
        if (StringUtils.isEmpty(cityId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide cityId");
            return result;
        }
        ResultData response = airqualityService.getHourlyCityAqi(cityId);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription("success to get the value of city pm2.5 in last 24 hour");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to get the value of city pm2.5 in last 24 hour");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("sorry,can not get any data!");
            return result;
        }
        return result;
    }

    //根据cityId给出那个城市过去一周的pm2.5记录
    @RequestMapping(value = "/city/daily/aqi", method = RequestMethod.GET)
    ResultData getCityDailyAqi(String cityId) {
        ResultData result = new ResultData();

        //check empty
        if (StringUtils.isEmpty(cityId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide cityId");
            return result;
        }

        ResultData response = airqualityService.getDailyCityAqi(cityId);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription("success to get city weekly airquality");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to get city weekly airquality");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("sorry,can not get any data!");
            return result;
        }

        return result;
    }


}
