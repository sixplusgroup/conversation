package finley.gmair.controller;

import finley.gmair.service.AirQualityService;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shenghaohu@hshvip.com
 * @date 2018/6/20
 */
@RestController
@RequestMapping("/management/airQuality")
public class AirQualityController {

    @Autowired
    private AirQualityService airQualityService;

    @GetMapping("/hourly/cityAqi")
    public ResultData allHourlyCityAqi() {
        return airQualityService.allHourlyCityAqi();
    }

    @GetMapping("/latest")
    ResultData getLatestCityAirQuality(){
        return airQualityService.getLatestCityAirQuality();
    }

    @GetMapping("/latest/{cityId}")
    ResultData getLatestCityAirQuality(@PathVariable String cityId){
        return airQualityService.getLatestCityAirQuality(cityId);
    }

    @GetMapping("/station/{stationId}")
    ResultData fetchLatestByStationId(@PathVariable String stationId){
        return airQualityService.fetchLatestByStationId(stationId);
    }

    @GetMapping("/station/city/{cityId}")
    ResultData fetchLatestByCityId(@PathVariable String cityId){
        return airQualityService.fetchLatestByCityId(cityId);
    }

    @GetMapping("/station/list")
    ResultData fetchLatestStationList(){
        return airQualityService.fetchLatestStationList();
    }
}