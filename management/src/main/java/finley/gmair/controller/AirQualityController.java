package finley.gmair.controller;

import finley.gmair.service.AirQualityService;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author shenghaohu@hshvip.com
 * @date 2018/6/20
 */
@CrossOrigin
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

    @GetMapping("/airquality/weekly/cityAqi/{cityId}")
    ResultData getWeeklyCityAqi(@PathVariable String cityId) {
        return airQualityService.getWeeklyCityAqi(cityId);
    }
}