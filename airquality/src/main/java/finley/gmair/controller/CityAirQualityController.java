package finley.gmair.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mysql.cj.xdevapi.JsonArray;
import finley.gmair.service.AirQualityStatisticService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.vo.air.CityAirQualityStatisticVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping(value = "/airquality")
public class CityAirQualityController {

    @Autowired
    private AirQualityStatisticService airQualityStatisticService;

    @CrossOrigin
    @GetMapping(value = "/latest")
    public ResultData getLatestCityAirQuality() {
        return airQualityStatisticService.fetchLatestAirQuality(new HashMap<>());
    }

    @CrossOrigin
    @GetMapping(value = "/latest/{cityId}")
    public ResultData getLatestCityAirQuality(@PathVariable String cityId) {
        Map<String, Object> condition = new HashMap<>();
        condition.put("cityId", cityId);
        return airQualityStatisticService.fetchLatestAirQuality(condition);
    }

    @RequestMapping(value = "/hourly/cityAqi", method = RequestMethod.POST)
    public ResultData scheduleHourlyCityAqi() {
        return airQualityStatisticService.handleAirQualityHourlyStatistic();
    }

    @CrossOrigin
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

        Timestamp last24Hour = new Timestamp(System.currentTimeMillis() / (1000 * 60 * 60) * (1000 * 60 * 60) - 24 * 60 * 60 * 1000);
        Timestamp lastHour = new Timestamp(System.currentTimeMillis() / (1000 * 60 * 60) * (1000 * 60 * 60));
        condition.put("cityId", cityId);
        condition.put("createTimeGTE", last24Hour);
        condition.put("createTimeLTE", lastHour);
        ResultData response = airQualityStatisticService.fetchAirQualityHourlyStatistic(condition);

        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("the airquality server is busy, try later!");
        } else {
            JSONArray jsonArray = (JSONArray) response.getData();
            List<CityAirQualityStatisticVo> list = new ArrayList<>();
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = (JSONObject) JSONObject.toJSON(jsonArray.get(i));
                CityAirQualityStatisticVo caqsvo = jsonObject.toJavaObject(CityAirQualityStatisticVo.class);
                caqsvo.setCreateTime(new Timestamp(caqsvo.getCreateTime().getTime() / (1000 * 60 * 60) * (1000 * 60 * 60)));
                list.add(caqsvo);
            }
            for (int i = 0; i < 24; i++) {
                if (list.size() == i || list.get(i).getCreateTime().getTime() != last24Hour.getTime() + (i+1) * 1000 * 60 * 60) {
                    list.add(i, new CityAirQualityStatisticVo(cityId, 0, new Timestamp(last24Hour.getTime() + (i+1) * 1000 * 60 * 60)));
                }
            }
            result.setData(list);
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
        long current = System.currentTimeMillis();//当前时间毫秒数
        long zero = (current / (1000 * 3600 * 24)) * (1000 * 3600 * 24) - TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
        Map<String, Object> condition = new HashMap<>();
        condition.put("cityId", cityId);
        condition.put("createTimeGTE", new Timestamp(zero - 7 * 24 * 60 * 60 * 1000));
        condition.put("createTimeLTE", new Timestamp(zero));
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

    @CrossOrigin
    @GetMapping("/weekly/cityAqi/{cityId}")
    public ResultData getWeeklyCityAqi(@PathVariable String cityId) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("cityId", cityId);
        LocalDate today = LocalDateTime.now().toLocalDate();
        LocalDate lastWeekDay = today.minusDays(7);
        condition.put("createTimeGTE", lastWeekDay);

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
}
