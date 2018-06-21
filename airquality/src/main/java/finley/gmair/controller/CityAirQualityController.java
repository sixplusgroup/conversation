package finley.gmair.controller;


import finley.gmair.service.AirQualityStatisticService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/airquality")
public class CityAirQualityController {

    @Autowired
    private AirQualityStatisticService airQualityStatisticService;

    @RequestMapping(value = "/hourly/cityAqi", method = RequestMethod.POST)
    public ResultData scheduleHourlyCityAqi() {
        return airQualityStatisticService.handleAirQualityHourlyStatistic();
    }

    @GetMapping(value = "/hourly/cityAqi")
    public ResultData getHourlyCityAqi() {

        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        Timestamp lastHourTime = new Timestamp(System.currentTimeMillis() - 3600000);
        condition.put("createTimeGTE", lastHourTime);

        ResultData response = airQualityStatisticService.fetchAirQualityHourlyStatistic(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("The airQuality server is busy, please try again");
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        return result;
    }

    @RequestMapping(value = "/hourly/cityAqi/{cityId}", method = RequestMethod.GET)
    public ResultData getHourlyCityAqi(@PathVariable("cityId") String cityId) {

        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("cityId", cityId);

        ResultData response = airQualityStatisticService.fetchAirQualityHourlyStatistic(condition);

        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("the airquality server is busy, try later!");
        } else {
            result.setData(response.getData());
        }
        return result;
    }

    @RequestMapping(value = "/daily/cityAqi", method = RequestMethod.POST)
    public ResultData scheduleDailyCityAqi() {
        return airQualityStatisticService.handleAirQualityDailyStatistic();
    }

    @RequestMapping(value = "/daily/cityAqi/{cityId}", method = RequestMethod.GET)
    public ResultData getDailyCityAqi(@PathVariable("cityId") String cityId) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("cityId", cityId);

        ResultData response = airQualityStatisticService.fetchAirQualityDailyStatistic(condition);

        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("the airquality server is busy, try later!");
        } else {
            result.setData(response.getData());
        }
        return result;
    }

    @RequestMapping(value = "/monthly/cityAqi", method = RequestMethod.POST)
    public ResultData scheduleMonthlyCityAqi() {

        return airQualityStatisticService.handleAirQualityMonthlyStatistic();
    }

    @RequestMapping(value = "/monthly/cityAqi/{cityId}", method = RequestMethod.GET)
    public ResultData getMonthlyCityAqi(@PathVariable("cityId") String cityId) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("cityId", cityId);

        ResultData response = airQualityStatisticService.fetchAirQualityMonthlyStatistic(condition);

        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("the airquality server is busy, try later!");
        } else {
            result.setData(response.getData());
        }
        return result;
    }
}
